package org.northshore.cbri.mapper;

import groovy.util.logging.Log4j
import de.tudarmstadt.ukp.dkpro.core.dictionaryannotator.PhraseTree

@Log4j
public class DictionaryModel {
	static class DictionaryEntry {
		String vocabulary
		String code
		String[] canonical
		Collection<String[]> variants = new ArrayList<>()
		Map<String, String> attrs = new HashMap<>()
	}
		
	private Map<Collection<String>, DictionaryEntry> entries = new HashMap<>()
	private PhraseTree phrases = new PhraseTree()

	public void add(final DictionaryEntry entry) {
		this.entries.put(entry.canonical, entry)
		this.phrases.addPhrase(entry.canonical)
		entry.variants.each {
			this.phrases.addPhrase(it)
		}
	}
	
	public Map<Collection<String>, DictionaryEntry> lookup(final String[] tokens) {
		Map<Collection<String>, DictionaryEntry> matches = new HashMap<>()
		for (int i = 0; i < tokens.length; i++) {
			String[] tokensToEnd = tokens[i, tokens.length - 1]
			String[] longestMatch = phrases.getLongestMatch(tokensToEnd)
			if (longestMatch != null) {
				matches[longestMatch] = entries[longestMatch]
			}
		}
		return matches;
	}
}
