package org.northshore.cbri.dict.trie;

import java.util.Collection;
import java.util.Map;

import groovy.util.logging.Log4j

import org.ahocorasick.trie.Trie
import org.northshore.cbri.dict.DictionaryModel.DictionaryEntry;
import org.northshore.cbri.dict.DictionaryModel.LookupMatch;

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

	Trie trie = new Trie()
	Map<String[], DictionaryEntry> entries = new HashMap<>()
	
	public DictionaryEntry get(String[] tokens) {
		return this.entries.get(join(tokens))
	}

	public void add(final DictionaryEntry entry) {
		this.trie.addKeyword(join(entry.canonical))
		this.entries.put(join(entry.canonical), entry)
		entry.variants.each {
			this.trie.addKeyword(join(it))
			this.entries.put(join(it), entry)
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

	private String join(String[] tokens) {
		StringJoiner joiner = new StringJoiner(' ')
		tokens.each {
			joiner.add(it)
		}
		return joiner.toString()
	}
}
