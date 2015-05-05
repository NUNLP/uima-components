package org.northshore.cbri.dict

import org.northshore.cbri.dict.phrase.PhraseDictionaryModel;

class DictionaryModelPool {
	private static Map<Integer, PhraseDictionaryModel> dicts;
	
	static {
		dicts = new HashMap<>()
	}

	public synchronized static void put(Integer id, PhraseDictionaryModel dict) {
		dicts[1] = dict
	}

	public synchronized static PhraseDictionaryModel get(Integer id) {
		return dicts[id]
	}
}
