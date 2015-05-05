package org.northshore.cbri.dict.trie;

import static org.junit.Assert.*
import groovy.util.logging.Log4j
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel

import org.apache.log4j.BasicConfigurator
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.northshore.cbri.dict.AbstractionSchema
import org.northshore.cbri.dict.trie.DictionaryModelFactory
import org.northshore.cbri.dict.trie.DictionaryModel.DictionaryEntry
import org.northshore.cbri.dict.trie.DictionaryModel.LookupMatch

import com.fasterxml.jackson.databind.ObjectMapper

@Log4j
class DictionaryModelTest {
	
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
	public void testDictModel() {
		String text = "The patient has a diagnosis of glioblastoma.  GBM does not have a good prognosis.  But I can't rule out meningioma."
		
		File dictFile = new File(this.class.getResource('/abstractionSchema/test-abstraction-schema.json').file)
		AbstractionSchema schema = mapper.readValue(dictFile, AbstractionSchema.class);
		assert schema != null
		
		TokenizerME tokenizer = new TokenizerME(new TokenizerModel(new File(this.class.getResource('/models/en-token.bin').file)))
		assert tokenizer != null
		
		DictionaryModel model = DictionaryModelFactory.make(schema, tokenizer, false)
		assert model != null
		
		String[] tokens = DictionaryModelFactory.tokenize(text, tokenizer)
		assert tokens.length == 24
		
		Collection<LookupMatch> matches = model.findMatches(tokens)
		assert matches.size() == 2
	}
	
	@Test
	public void testCaseInsensitiveDictModel() {
		String text = "The patient has a diagnosis of Glioblastoma.  GBM does not have a good prognosis.  But I can't rule out meninGioma."
		
		File dictFile = new File(this.class.getResource('/abstractionSchema/test-abstraction-schema.json').file)
		AbstractionSchema schema = mapper.readValue(dictFile, AbstractionSchema.class);
		assert schema != null
		
		TokenizerME tokenizer = new TokenizerME(new TokenizerModel(new File(this.class.getResource('/models/en-token.bin').file)))
		assert tokenizer != null
		
		DictionaryModel model = DictionaryModelFactory.make(schema, tokenizer, true)
		assert model != null
		
		String[] tokens = DictionaryModelFactory.tokenize(text, tokenizer)
		assert tokens.length == 24
		
		Collection<LookupMatch> matches = model.findMatches(tokens)
		assert matches.size() == 3
	}

}
