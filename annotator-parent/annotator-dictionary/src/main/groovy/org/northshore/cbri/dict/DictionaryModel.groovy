package org.northshore.cbri.dict;

import groovy.util.logging.Log4j

@Log4j
public class DictionaryModel {
	static class DictionaryEntry {
		String vocabulary
		String code
		String[] canonical
		Collection<String[]> variants = new ArrayList<>()
		Map<String, String> attrs = new HashMap<>()
	}
	
	static class LookupMatch {
		Integer begin
		Integer end
		DictionaryEntry entry;
	}
		
	private Map<String[], DictionaryEntry> entries = new HashMap<>()
	private PhraseTree phrases = new PhraseTree()

	public DictionaryEntry get(String[] tokens) {
		return this.entries.get(Arrays.toString(tokens))
	}

	public void add(final DictionaryEntry entry) {
		this.phrases.addPhrase(entry.canonical)
		this.entries.put(Arrays.toString(entry.canonical), entry)
		entry.variants.each {
			this.phrases.addPhrase(it)
			this.entries.put(Arrays.toString(it), entry)
		}
	}
		
	public Collection<LookupMatch> findMatches(final String[] tokens) {
		Collection<LookupMatch> matches = new ArrayList<>()
		
		for (int i = 0; i < tokens.length; i++) {
			String[] tokensToEnd = tokens[i, tokens.length - 1]
			Integer endMatchPosition = phrases.getLongestMatch(tokensToEnd)
			if (endMatchPosition != null) {
				String[] matchedTokens = Arrays.copyOfRange(tokensToEnd, 0, endMatchPosition)
				matches << new LookupMatch(
					begin:i,
					end:(i+endMatchPosition),
					entry:entries.get(Arrays.toString(matchedTokens))
					)
			}
		}
		
		return matches;
	}
}
