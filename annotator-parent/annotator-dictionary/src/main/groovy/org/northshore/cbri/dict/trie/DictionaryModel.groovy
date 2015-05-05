package org.northshore.cbri.dict.trie;

import groovy.util.logging.Log4j

import org.ahocorasick.trie.Emit
import org.ahocorasick.trie.Trie

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

	Trie trie;
	Map<String[], DictionaryEntry> entries;
	
	public DictionaryModel(Boolean caseInsensitive) {
		if (caseInsensitive) { trie = new Trie().caseInsensitive(); }
		else { trie = new Trie() }
		entries = new HashMap<>()
	}
	
	public DictionaryEntry get(String[] tokens) {
		return this.entries.get(join(tokens))
	}

	public void add(final DictionaryEntry entry) {
		this.trie.addKeyword(join(entry.canonical))
		this.entries.put(join(entry.canonical), entry)
		entry.variants.each {
			String phrase = join(it)
			println "adding phrase: $phrase"
			this.trie.addKeyword(join(it))
			this.entries.put(join(it), entry)
		}
	}
	
	public Collection<LookupMatch> findMatches(final String[] tokens) {
		Collection<LookupMatch> matches = new ArrayList<>()
		
		Collection<Emit> emits = trie.parseText(join(tokens))
		emits.each { 
			matches << new LookupMatch(
				begin:it.start, 
				end:it.end, 
				entry:this.get(join(it.keyword)))
		}
		
		return matches;
	}

	private String join(String[] tokens, String sep=' ') {
		StringJoiner joiner = new StringJoiner(sep)
		tokens.each {
			joiner.add(it)
		}
		return joiner.toString()
	}
}
