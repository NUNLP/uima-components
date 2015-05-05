package org.northshore.cbri.dict.trie

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.util.Span

import org.northshore.cbri.dict.AbstractionSchema
import org.northshore.cbri.dict.ObjectValue
import org.northshore.cbri.dict.ObjectValueVariant

class DictionaryModelFactory {		
	/**
	 * 
	 * @param schema
	 * @param tokenizer
	 * @return
	 */
	static public DictionaryModel make(AbstractionSchema schema, TokenizerME tokenizer, Boolean caseInsensitive) {
		DictionaryModel model = new DictionaryModel(caseInsensitive)
		DictionaryModel.DictionaryEntry entry;		
		schema.object_values.each { ObjectValue objVal ->
			entry = new DictionaryModel.DictionaryEntry()
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
	
	/**
	 * 
	 * @param phrase
	 * @param tokenizer
	 * @return
	 */
	static public String[] tokenize(String phrase, TokenizerME tokenizer) {
		Collection<String> tokens = new ArrayList<>()
		Span[] tokenSpans = tokenizer.tokenizePos(phrase)
		tokenSpans.each { Span span ->
			tokens << phrase.substring(span.getStart(), span.getEnd())
		}
		return tokens
	}
}
