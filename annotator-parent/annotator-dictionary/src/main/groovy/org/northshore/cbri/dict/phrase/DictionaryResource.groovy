package org.northshore.cbri.dict.phrase

import org.apache.uima.resource.DataResource
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;

class DictionaryResource implements SharedResourceObject {
	
	DictionaryModel model;

	@Override
	public void load(DataResource aData) throws ResourceInitializationException {

	}

	DictionaryModel getModel() { return model }
}
