package org.northshore.cbri.dict.phrase;

import groovy.util.logging.Log4j

import org.northshore.cbri.dict.DictionaryEntry
import org.northshore.cbri.dict.DictionaryModel
import org.northshore.cbri.dict.LookupMatch

import com.wcohen.ss.JaroWinkler
import com.wcohen.ss.SoftTFIDF
import com.wcohen.ss.api.Tokenizer
import com.wcohen.ss.tokens.SimpleTokenizer

@Log4j
public class PhraseDictionaryModel implements DictionaryModel {
		
	private Map<String[], DictionaryEntry> entries = new HashMap<>()
	private PhraseTree phrases = new PhraseTree()
	
	private Boolean caseInsensitive;

	public PhraseDictionaryModel(Boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}
	
	@Override
	public DictionaryEntry get(String[] tokens) {
		return this.entries.get(this.join(tokens))
	}

	@Override
	public void add(final DictionaryEntry entry) {
		this.phrases.addPhrase(this.transformArray(entry.canonical))
		this.entries.put(this.join(entry.canonical), entry)
		entry.variants.each {
			this.phrases.addPhrase(this.transformArray(it))
			this.entries.put(this.join(it), entry)
		}
	}
		
	@Override
	public Collection<LookupMatch> findMatches(final String[] tokens) {
		Collection<LookupMatch> matches = new ArrayList<>()
		
		for (int i = 0; i < tokens.length; i++) {
			String[] tokensToEnd = tokens[i, tokens.length - 1]
			tokensToEnd = transformArray(tokensToEnd)
			Integer endMatchPosition = phrases.getLongestMatch(tokensToEnd)
			if (endMatchPosition != null) {
				String[] matchedTokens = Arrays.copyOfRange(tokensToEnd, 0, endMatchPosition)
				matches << new LookupMatch(
					begin:i,
					end:(i+endMatchPosition),
					entry:entries.get(this.join(matchedTokens))
					)
			}
		}
		
		return matches;
	}
	
	@Override
	public Collection<LookupMatch> findMatches(String[] tokens, Double tolerance) {
		Collection<LookupMatch> matches = new ArrayList<>()
		
		Tokenizer tokenizer = new SimpleTokenizer(false,true);
		SoftTFIDF distance = new SoftTFIDF(tokenizer, new JaroWinkler(), tolerance);
		
		return matches;
	}


	private String[] transformArray(String[] tokens) {
		tokens.eachWithIndex { tok, i ->
			tokens[i] = transform(tok)
		}
		return tokens
	}

	private String transform(String token) {
		if (caseInsensitive) {
			token = token.toLowerCase()
		}
		return token
	}
	
	private String join(String[] tokens, String sep=' ') {
		StringJoiner joiner = new StringJoiner(sep)
		tokens.each {
			joiner.add(this.transform(it))
		}
		return joiner.toString()
	}
}
