package org.northshore.cbri.dict.trie;

import java.util.Map;

import groovy.util.logging.Log4j

import org.ahocorasick.trie.Trie
import org.northshore.cbri.dict.DictionaryModel.DictionaryEntry;

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

	public void add(final DictionaryEntry entry) {
		this.trie.addKeyword(join(entry.canonical))
		this.entries.put(join(entry.canonical), entry)
		this.trie.addKeyword(entry.canonical)
		entry.variants.each {
			this.trie.addKeyword(join(it))
			this.entries.put(join(it), entry)
		}
	}

	String join(String[] tokens) {
		StringJoiner joiner = new StringJoiner(' ')
		tokens.each {
			joiner.add(it)
		}
		return joiner.toString()
	}
}
