package org.northshore.cbri.dict

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.util.Span

import org.northshore.cbri.dict.phrase.PhraseDictionaryModel
import org.northshore.cbri.dict.trie.TrieDictionaryModel

class DictionaryModelFactory {
	
	public static String DICT_MODEL_TYPE_PHRASE = "Phrase"
	public static String DICT_MODEL_TYPE_TRIE = "Trie"
	
	static public DictionaryModel make(final String dictModelType, 
		final AbstractionSchema schema, 
		final TokenizerME tokenizer, 
		final Boolean caseInsensitive) {
		
		DictionaryModel model;
		
		switch (dictModelType) {
			case DICT_MODEL_TYPE_TRIE: 
			model = new TrieDictionaryModel(caseInsensitive)
			break;
			
			case DICT_MODEL_TYPE_PHRASE:
			model = new PhraseDictionaryModel(caseInsensitive)
			break
		}
				
		schema.object_values.each { ObjectValue objVal ->
			DictionaryEntry entry = new DictionaryEntry()
			entry.vocabulary = objVal.vocabulary
			entry.code = objVal.vocabulary_code
			entry.canonical = tokenize(objVal.value, tokenizer)
			objVal.object_value_variants.each { ObjectValueVariant variant ->
				entry.variants << tokenize(variant.value, tokenizer)
			}
			model.add(entry)
		}
		return model
	}
	
	static public String[] tokenize(String phrase, TokenizerME tokenizer) {
		Collection<String> tokens = new ArrayList<>()
		Span[] tokenSpans = tokenizer.tokenizePos(phrase)
		tokenSpans.each { Span span ->
			tokens << phrase.substring(span.getStart(), span.getEnd())
		}
		return tokens
	}
}
