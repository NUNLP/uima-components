package org.northshore.cbri.dict

import org.northshore.cbri.dict.phrase.PhraseDictionaryModel

class DictionaryModelPool {
	private static Map<Integer, PhraseDictionaryModel> dicts;
	
	static {
		dicts = new HashMap<>()
	}

	public synchronized static void put(Integer id, DictionaryModel dict) {
		dicts[1] = dict
	}

	public synchronized static DictionaryModel get(Integer id) {
		return dicts[id]
	}
}
