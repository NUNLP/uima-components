package org.northshore.cbri.dict;

import static org.junit.Assert.*
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
import org.northshore.cbri.dict.DictionaryModel.DictionaryEntry
import org.northshore.cbri.dsl.UIMAUtil
import org.northshore.cbri.token.TokenAnnotator

import com.fasterxml.jackson.databind.ObjectMapper

@Log4j
class ConceptMapperTest {
	static TEST_TEXT = "The patient has a diagnosis of glioblastoma.  GBM does not have a good prognosis.  But I can't rule out meningioma."
	
	TokenizerME tokenizer;
	DictionaryModel model;

	@BeforeClass
	public static void setupClass() {
		BasicConfigurator.configure()
	}

	@Before
	public void setUp() throws Exception {
		File dictFile = new File(this.class.getResource('/abstractionSchema/test-abstraction-schema.json').file)
		ObjectMapper mapper = new ObjectMapper()
		AbstractionSchema schema = mapper.readValue(dictFile, AbstractionSchema.class);
		assert schema != null
		
		this.tokenizer = new TokenizerME(new TokenizerModel(new File(this.class.getResource('/models/en-token.bin').file)))
		assert tokenizer != null
		
		this.model = DictionaryModelFactory.make(schema, tokenizer)
		assert model != null

	}
	
	@Test
	public void testDictModel() {		
		String[] tokens = DictionaryModelFactory.tokenize(TEST_TEXT,
			tokenizer)
		assert tokens.length == 24
		
		Map<Collection<String>, DictionaryEntry> matches = this.model.findMatches(tokens)
		assert matches.size() == 2
		matches.each { k, v -> println "Match: ${k}" }
	}

	@Test
	public void testUIMAPipeline() {
		// --------------------------------------------------------------------
		// load dictionary
		// --------------------------------------------------------------------
		DictionaryModelPool.put(1, this.model)

		// --------------------------------------------------------------------
		// external resources
		// --------------------------------------------------------------------
			
		ExternalResourceDescription tokenModelResDesc = ExternalResourceFactory.createExternalResourceDescription(
			TokenizerModelResourceImpl, "file:models/en-token.bin");
			
		// --------------------------------------------------------------------
		// analysis engines
		// --------------------------------------------------------------------
		
		AnalysisEngineDescription tokenDesc = AnalysisEngineFactory.createEngineDescription(
				TokenAnnotator, 
				TokenAnnotator.TOKEN_MODEL_KEY, tokenModelResDesc)
		AnalysisEngine tokenizer = AnalysisEngineFactory.createEngine(tokenDesc)

		AnalysisEngineDescription mapperDesc = AnalysisEngineFactory.createEngineDescription(
			DictionaryAnnotator,
			DictionaryAnnotator.PARAM_DICTIONARY_ID, 1)
		AnalysisEngine mapper = AnalysisEngineFactory.createEngine(mapperDesc)

		// --------------------------------------------------------------------
		// test
		// --------------------------------------------------------------------
		
		JCas jcas = JCasFactory.createJCas()
		jcas.setDocumentText(TEST_TEXT)
		UIMAUtil.JCas = jcas
		UIMAUtil.create(type:Sentence, begin:0, end:TEST_TEXT.length()-1)
		
		tokenizer.process(jcas)
		mapper.process(jcas)
		
	}
}
