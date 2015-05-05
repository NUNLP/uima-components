package org.northshore.cbri.dict

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import org.northshore.cbri.dict.DictionaryEntry

@ToString(includeNames=true)
@EqualsAndHashCode
class LookupMatch {
	Integer begin
	Integer end
	DictionaryEntry entry;
}
