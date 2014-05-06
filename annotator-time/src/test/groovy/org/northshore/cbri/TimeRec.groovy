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

class TimeRec {
    static void main(args) {
        Properties props = new Properties();
        AnnotationPipeline pipeline = new AnnotationPipeline();
        pipeline.addAnnotator(new PTBTokenizerAnnotator(false));
        pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
        pipeline.addAnnotator(new POSTaggerAnnotator(false));
        pipeline.addAnnotator(new TimeAnnotator('sutime', props));
        
        Collection<String> texts = ['The patient had an onset of diabetes 14 years ago on May 6, 2000. The patient has had diabetes for 6 years']

        for (String text : texts) {
            Annotation annotation = new Annotation(text);
            annotation.set(CoreAnnotations.DocDateAnnotation.class, '2014-05-06');
            pipeline.annotate(annotation);
            System.out.println(annotation.get(CoreAnnotations.TextAnnotation.class));
            List timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
            for (CoreMap cm : timexAnnsAll) {
                List tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
                System.out.println(cm.toString() + ' [from char offset ' +
                        tokens.get(0).get(CoreAnnotations.CharacterOffsetBeginAnnotation.class) +
                        ' to ' + tokens.get(tokens.size() - 1).get(CoreAnnotations.CharacterOffsetEndAnnotation.class) + ']' +
                        ' --> ' + cm.get(TimeExpression.Annotation.class).getTemporal());
            }
            System.out.println('--');
        }
    }
}
