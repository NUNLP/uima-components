package org.northshore.cbri.dict

import static org.northshore.cbri.dsl.UIMAUtil.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.apache.uima.resource.ResourceInitializationException
import org.northshore.cbri.dsl.UIMAUtil
import org.northshore.cbri.type.DictMatch

@Log4j
public class DictionaryAnnotator extends JCasAnnotator_ImplBase {
	    
    public static final String PARAM_DICTIONARY_ID = 'dictionaryId'
    @ConfigurationParameter(name='dictionaryId', mandatory=false)
    private Integer dictionaryId

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext)
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException
    {
		logger.info "Dictionary to load: ${dictionaryId}"
		DictionaryModel dict = DictionaryModelPool.get(dictionaryId)
		if (dict == null) {
			logger.warn "No dictionary loaded with id: ${dictionaryId}"
			return;
		}
		
		UIMAUtil.jcas = jcas
		select(type:Sentence).each { Sentence sent ->
			Collection<Annotation> anns = select(type:BaseToken, filter:coveredBy(sent))
			Collection<String> tokens = new ArrayList<>()
			anns.each { Annotation ann ->
				tokens << ann.coveredText
			}
			Map results = dict.findMatches(tokens as String[])
			results.each { k, v ->
				UIMAUtil.create(type:DictMatch)
			}
		}
    }
}
