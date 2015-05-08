package org.northshore.cbri.dict;

import java.util.Collection;

public interface DictionaryModel {
	
	public DictionaryEntry get (String[] tokens);
	
	public void add (final DictionaryEntry entry);
	
	public Collection<LookupMatch> findMatches (final String[] tokens);
	
	public Collection<LookupMatch> findMatches (final String[] tokens, Double tolerance);
}
