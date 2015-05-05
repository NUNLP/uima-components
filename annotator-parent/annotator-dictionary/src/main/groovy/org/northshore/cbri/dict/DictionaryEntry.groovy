package org.northshore.cbri.dict

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames=true)
@EqualsAndHashCode
class DictionaryEntry {
	String vocabulary
	String code
	String[] canonical
	Collection<String[]> variants = new ArrayList<>()
	Map<String, String> attrs = new HashMap<>()
}
