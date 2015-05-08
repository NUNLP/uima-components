package org.northshore.cbri.dict;

import static org.junit.Assert.*
import groovy.json.JsonBuilder
import groovy.util.logging.Log4j
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.uima.tokenize.TokenizerModelResourceImpl

import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.factory.JCasFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ExternalResourceDescription
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.northshore.cbri.dict.phrase.PhraseDictionaryModel
import org.northshore.cbri.dsl.UIMAUtil
import org.northshore.cbri.token.TokenAnnotator
import org.northshore.cbri.type.DictMatch

import com.fasterxml.jackson.databind.ObjectMapper

@Log4j
class DictionaryAnnotatorTest {
	static ObjectMapper mapper;

	@BeforeClass
	public static void setupClass() {
		BasicConfigurator.configure()
		mapper = new ObjectMapper()
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPhraseDictModel() {
		String text = "The patient has a diagnosis of glioblastoma.  GBM does not have a good prognosis.  But I can't rule out meningioma."

		File dictFile = new File(this.class.getResource('/abstractionSchema/test-abstraction-schema.json').file)
		AbstractionSchema schema = mapper.readValue(dictFile, AbstractionSchema.class);
		assert schema != null

		TokenizerME tokenizer = new TokenizerME(new TokenizerModel(new File(this.class.getResource('/models/en-token.bin').file)))
		assert tokenizer != null

		DictionaryModel model = DictionaryModelFactory.make(DictionaryModelFactory.DICT_MODEL_TYPE_PHRASE, schema, tokenizer, true)
		assert model != null

		DictionaryEntry entry = model.get(['glioblastoma'] as String[])
		assert entry != null

		String[] tokens = DictionaryModelFactory.tokenize(text, tokenizer)
		assert tokens.length == 24

		Collection<LookupMatch> matches = model.findMatches(tokens)
		matches.each { println "Match: ${new JsonBuilder(it).toString()}" }
		assert matches.size() == 3
	}

	@Test
	public void testTrieDictModel() {
		String text = "The patient has a diagnosis of glioblastoma.  GBM does not have a good prognosis.  But I can't rule out meningioma."

		File dictFile = new File(this.class.getResource('/abstractionSchema/test-abstraction-schema.json').file)
		AbstractionSchema schema = mapper.readValue(dictFile, AbstractionSchema.class);
		assert schema != null

		TokenizerME tokenizer = new TokenizerME(new TokenizerModel(new File(this.class.getResource('/models/en-token.bin').file)))
		assert tokenizer != null

		DictionaryModel model = DictionaryModelFactory.make(DictionaryModelFactory.DICT_MODEL_TYPE_TRIE, schema, tokenizer, true)
		assert model != null

		String[] tokens = DictionaryModelFactory.tokenize(text, tokenizer)
		tokens.eachWithIndex { tok, i ->
			tokens[i] = tok.toLowerCase()
		}
		assert tokens.length == 24

		DictionaryEntry entry = model.get(['glioblastoma'] as String[])
		assert entry != null

		Collection<LookupMatch> matches = model.findMatches(tokens)
		matches.each { println "Match: ${new JsonBuilder(it).toString()}" }
		assert matches.size() == 3
	}

	@Test
	public void testUIMAPipeline() {
		String text = "The patient has a diagnosis of glioblastoma.  GBM does not have a good prognosis.  But I can't rule out meningioma."

		// --------------------------------------------------------------------
		// load dictionary model
		// --------------------------------------------------------------------
		File dictFile = new File(this.class.getResource('/abstractionSchema/test-abstraction-schema.json').file)
		AbstractionSchema schema = mapper.readValue(dictFile, AbstractionSchema.class);
		assert schema != null

		TokenizerME tokenizerME = new TokenizerME(new TokenizerModel(new File(this.class.getResource('/models/en-token.bin').file)))
		assert tokenizerME != null

		DictionaryModel model1 = DictionaryModelFactory.make(DictionaryModelFactory.DICT_MODEL_TYPE_PHRASE, schema, tokenizerME, true)
		assert model1 != null

		DictionaryModel model2 = DictionaryModelFactory.make(DictionaryModelFactory.DICT_MODEL_TYPE_PHRASE, schema, tokenizerME, true)
		assert model2 != null

		DictionaryModelPool.put(1, model1)
		DictionaryModelPool.put(2, model2)

		// --------------------------------------------------------------------
		// external resources
		// --------------------------------------------------------------------

		ExternalResourceDescription tokenModelResDesc = ExternalResourceFactory.createExternalResourceDescription(
				TokenizerModelResourceImpl, "file:models/en-token.bin")

		// --------------------------------------------------------------------
		// analysis engines
		// --------------------------------------------------------------------

		AnalysisEngineDescription tokenDesc = AnalysisEngineFactory.createEngineDescription(
				TokenAnnotator,
				TokenAnnotator.TOKEN_MODEL_KEY, tokenModelResDesc)
		AnalysisEngine tokenizer = AnalysisEngineFactory.createEngine(tokenDesc)

		AnalysisEngineDescription dictDesc = AnalysisEngineFactory.createEngineDescription(
				DictionaryAnnotator,
				DictionaryAnnotator.PARAM_DICTIONARY_ID, 1,
				)
		AnalysisEngine dict = AnalysisEngineFactory.createEngine(dictDesc)

		// --------------------------------------------------------------------
		// test model 1
		// --------------------------------------------------------------------

		JCas jcas = JCasFactory.createJCas()
		jcas.setDocumentText(text)
		UIMAUtil.JCas = jcas
		UIMAUtil.create(type:Sentence, begin:0, end:text.size()-1)

		tokenizer.process(jcas)
		dict.process(jcas)

		Collection<DictMatch> matches = UIMAUtil.select(type:DictMatch)
		matches.each { DictMatch m -> println "Match found: ${m.matchedTokens}" }
		assert matches.size() == 3

		// --------------------------------------------------------------------
		// test model 2
		// --------------------------------------------------------------------

		dict.setConfigParameterValue(
				DictionaryAnnotator.PARAM_DICTIONARY_ID, 2
				)
		dict.reconfigure()

		jcas.reset()
		jcas.setDocumentText(text)
		UIMAUtil.JCas = jcas
		UIMAUtil.create(type:Sentence, begin:0, end:text.size()-1)

		tokenizer.process(jcas)
		dict.process(jcas)

		matches = UIMAUtil.select(type:DictMatch)
		matches.each { DictMatch m -> println "Match found: ${m.matchedTokens}" }
		assert matches.size() == 3
	}
}
