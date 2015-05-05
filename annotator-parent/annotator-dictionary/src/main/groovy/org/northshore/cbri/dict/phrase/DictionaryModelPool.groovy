package org.northshore.cbri.dict.phrase

class DictionaryModelPool {
	private static Map<Integer, DictionaryModel> dicts;
	
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
