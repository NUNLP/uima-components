package org.northshore.cbri.dict.trie;

import java.util.Collection;

import groovy.util.logging.Log4j

import org.ahocorasick.trie.Emit
import org.ahocorasick.trie.Trie
import org.northshore.cbri.dict.DictionaryEntry
import org.northshore.cbri.dict.DictionaryModel
import org.northshore.cbri.dict.LookupMatch

@Log4j
public class TrieDictionaryModel implements DictionaryModel {

	Trie trie;
	Map<String[], DictionaryEntry> entries;
	
	public TrieDictionaryModel(Boolean caseInsensitive) {
		if (caseInsensitive) { trie = new Trie().caseInsensitive(); }
		else { trie = new Trie() }
		entries = new HashMap<>()
	}
	
	@Override
	public DictionaryEntry get(String[] tokens) {
		return this.entries.get(join(tokens))
	}

	@Override
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
	
	@Override
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
	
	@Override
	public Collection<LookupMatch> findMatches(String[] tokens, Double tolerance) {
		Collection<LookupMatch> matches = new ArrayList<>()
				
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
