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

	static TEST_TEXT = "The patient has a diagnosis of glioblastoma.  GBM does not have a good prognosis.  But I can't rule out meningioma."
	
	static ObjectMapper mapper;
		
	TokenizerME tokenizer;
	DictionaryModel model;

	@BeforeClass
	public static void setupClass() {
		BasicConfigurator.configure()
		mapper = new ObjectMapper()
	}

	@Before
	public void setUp() throws Exception {
		File dictFile = new File(this.class.getResource('/abstractionSchema/test-abstraction-schema.json').file)
		AbstractionSchema schema = mapper.readValue(dictFile, AbstractionSchema.class);
		assert schema != null
		
		this.tokenizer = new TokenizerME(new TokenizerModel(new File(this.class.getResource('/models/en-token.bin').file)))
		assert tokenizer != null
		
		this.model = DictionaryModelFactory.make(schema, tokenizer)
		assert model != null
	}
	
	@Test
	public void testDictModel() {		
		String[] tokens = DictionaryModelFactory.tokenize(TEST_TEXT, tokenizer)
		assert tokens.length == 24
		
		DictionaryEntry entry = this.model.get(['glioblastoma'] as String[])
		assert entry != null
		
		Collection<LookupMatch> matches = this.model.findMatches(tokens)
		assert matches.size() == 2
	}

}
