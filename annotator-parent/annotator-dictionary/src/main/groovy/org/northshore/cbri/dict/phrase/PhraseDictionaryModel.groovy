package org.northshore.cbri.dict.phrase;

import groovy.util.logging.Log4j

import org.northshore.cbri.dict.DictionaryEntry
import org.northshore.cbri.dict.DictionaryModel
import org.northshore.cbri.dict.LookupMatch

@Log4j
public class PhraseDictionaryModel implements DictionaryModel {
		
	private Map<String[], DictionaryEntry> entries = new HashMap<>()
	private PhraseTree phrases = new PhraseTree()
	
	private Boolean caseInsensitive;

	public PhraseDictionaryModel(Boolean caseInsensitive) {
		caseInsensitive = caseInsensitive;
	}
	
	@Override
	public DictionaryEntry get(String[] tokens) {
		return this.entries.get(Arrays.toString(tokens))
	}

	@Override
	public void add(final DictionaryEntry entry) {
		this.phrases.addPhrase(entry.canonical)
		this.entries.put(Arrays.toString(entry.canonical), entry)
		entry.variants.each {
			this.phrases.addPhrase(it)
			this.entries.put(Arrays.toString(it), entry)
		}
	}
		
	@Override
	public Collection<LookupMatch> findMatches(final String[] tokens) {
		Collection<LookupMatch> matches = new ArrayList<>()
		
		for (int i = 0; i < tokens.length; i++) {
			String[] tokensToEnd = tokens[i, tokens.length - 1]
			if (caseInsensitive) {
				tokensToEnd.eachWithIndex { token, idx ->
					tokensToEnd[idx] = token.toLowerCase()
				}
			}
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
