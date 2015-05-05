package org.northshore.cbri.dict.phrase

import org.northshore.cbri.dict.AbstractionSchema;
import org.northshore.cbri.dict.ObjectValue;
import org.northshore.cbri.dict.ObjectValueVariant;

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.util.Span

class DictionaryModelFactory {
	/**
	 * 
	 * @param schema
	 * @param tokenizer
	 * @return
	 */
	static public DictionaryModel make(AbstractionSchema schema, TokenizerME tokenizer, Boolean caseInsensitive) {
		DictionaryModel model = new DictionaryModel()
		model.PARAM_CASE_INSENSITIVE = caseInsensitive
		DictionaryModel.DictionaryEntry entry;		
		schema.object_values.each { ObjectValue objVal ->
			entry = new DictionaryModel.DictionaryEntry()
			entry.vocabulary = objVal.vocabulary
			entry.code = objVal.vocabulary_code
			entry.canonical = tokenize(objVal.value, tokenizer, caseInsensitive)
			objVal.object_value_variants.each { ObjectValueVariant variant ->
				entry.variants << tokenize(variant.value, tokenizer, caseInsensitive)
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
	static public String[] tokenize(String phrase, TokenizerME tokenizer, Boolean caseInsensitive) {
		Collection<String> tokens = new ArrayList<>()
		Span[] tokenSpans = tokenizer.tokenizePos(phrase)
		tokenSpans.each { Span span ->
			tokens << (caseInsensitive ? phrase.substring(span.getStart(), span.getEnd()).toLowerCase() :
				phrase.substring(span.getStart(), span.getEnd()))
		}
		return tokens
	}
}
