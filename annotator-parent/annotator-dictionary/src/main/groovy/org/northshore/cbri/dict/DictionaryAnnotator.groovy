package org.northshore.cbri.dict

import static org.northshore.cbri.dsl.UIMAUtil.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.fit.descriptor.ExternalResource
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import org.apache.uima.jcas.tcas.Annotation
import org.apache.uima.resource.ResourceInitializationException
import org.northshore.cbri.dict.DictionaryModel.LookupMatch
import org.northshore.cbri.dsl.UIMAUtil
import org.northshore.cbri.type.DictMatch

@Log4j
public class DictionaryAnnotator extends JCasAnnotator_ImplBase {
	static {
		DictMatch.metaClass.getMatchedTokens = {
			return (delegate.matched == null ? []:
			org.apache.uima.fit.util.JCasUtil.select(delegate.matched, Annotation))
		}
		DictMatch.metaClass.setMatchedTokens = { anns ->
			if (anns == null) {
				return;
			}
			FSArray array = new FSArray(jcas, anns.size())
			int i = 0
			anns.each {
				array.set(i, it)
				i += 1
			}
			delegate.matched = array
		}
	}

//	final static String DIC_RESROURCE_KEY = "dictResource";
//	@ExternalResource(key = "")
//	private DictionaryResource dictResource;


	public static final String PARAM_DICTIONARY_ID = 'dictionaryId'
	@ConfigurationParameter(name='dictionaryId', mandatory=false)
	private Integer dictionaryId

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext)
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		logger.info "Loading dictionary: ${dictionaryId}"
		DictionaryModel dict = DictionaryModelPool.get(dictionaryId)
		if (dict == null) {
			logger.warn "No dictionary available with id: ${dictionaryId}"
			return;
		}

		UIMAUtil.jcas = jcas
		select(type:Sentence).each { Sentence sent ->
			Collection<Annotation> anns = select(type:BaseToken, filter:coveredBy(sent))
			Collection<String> tokens = new ArrayList<>()
			anns.each { Annotation ann ->
				tokens << ann.coveredText
			}
			Collection<LookupMatch> matches = dict.findMatches(tokens as String[])
			matches.each { LookupMatch m ->
				Collection<Annotation> matched = new ArrayList<>()
				for (int i = m.begin; i < m.end; i++) {
					matched << anns.get(i)
				}
				UIMAUtil.create(type:DictMatch,
				canonical:m.entry.canonical,
				code:m.entry.code,
				vocabulary:m.entry.vocabulary,
				container:sent,
				matchedTokens:matched
				)
			}
		}
	}
}
