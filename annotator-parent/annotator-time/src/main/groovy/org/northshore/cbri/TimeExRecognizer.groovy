package org.northshore.cbri

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.AnnotationPipeline
import edu.stanford.nlp.pipeline.POSTaggerAnnotator
import edu.stanford.nlp.pipeline.PTBTokenizerAnnotator
import edu.stanford.nlp.pipeline.WordsToSentencesAnnotator
import edu.stanford.nlp.time.TimeAnnotations
import edu.stanford.nlp.time.TimeAnnotator
import edu.stanford.nlp.time.TimeExpression
import edu.stanford.nlp.util.CoreMap
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.textsem.*
import org.apache.log4j.Level
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException

@Log4j
class TimeExRecognizer extends JCasAnnotator_ImplBase {
    public static final String TIMEREC_ANCHOR_DATE = 'anchorDate'
    
    @ConfigurationParameter(name = 'anchorDate', mandatory = false, description = 'Anchor date')
    private String segmentsToParse
    
    private AnnotationPipeline pipeline;
    
    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext)
        
        Properties props = new Properties()
        pipeline = new AnnotationPipeline()
        pipeline.addAnnotator(new PTBTokenizerAnnotator(false))
        pipeline.addAnnotator(new WordsToSentencesAnnotator(false))
        pipeline.addAnnotator(new POSTaggerAnnotator(false))
        pipeline.addAnnotator(new TimeAnnotator('sutime', props))
    }
    
    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        log.setLevel(Level.INFO)
        
        Annotation annotation = new Annotation(jcas.documentText)
        annotation.set(CoreAnnotations.DocDateAnnotation, '2014-05-06')
        this.pipeline.annotate(annotation)
                
        List timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations)
        timexAnnsAll.each { CoreMap cm ->
            List tokens = cm.get(CoreAnnotations.TokensAnnotation)
            TimeMention tm = new TimeMention(jcas)
            
//            println cm.toString() + ' [from char offset ' +
//                    tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation) +
//                    ' to ' + tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation) + ']' +
//                    ' --> ' + cm.get(TimeExpression.Annotation).getTemporal()
        }
    }
}
