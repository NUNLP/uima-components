package org.northshore.cbri.dict;

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
		
	private Map<String[], DictionaryEntry> entries = new HashMap<>()
	private PhraseTree phrases = new PhraseTree()

	public DictionaryEntry get(String[] tokens) {
		return this.entries.get(Arrays.toString(tokens))
	}

	public void add(final DictionaryEntry entry) {
		this.entries.put(entry.canonical, entry)
		this.phrases.addPhrase(entry.canonical)
		entry.variants.each {
			this.phrases.addPhrase(it)
			this.entries.put(Arrays.toString(it), entry)
		}
	}
		
	public Map<Collection<String>, DictionaryEntry> findMatches(final String[] tokens) {
		Map<String[], DictionaryEntry> matches = new HashMap<>()
		for (int i = 0; i < tokens.length; i++) {
			String[] tokensToEnd = tokens[i, tokens.length - 1]
			String[] longestMatch = phrases.getLongestMatch(tokensToEnd)
			if (longestMatch != null) {
				matches[Arrays.toString(longestMatch)] = entries.get(Arrays.toString(longestMatch))
			}
		}
		return matches;
	}
}
