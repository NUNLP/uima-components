package org.northshore.cbri.dict

import org.apache.uima.resource.DataResource
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.SharedResourceObject;
import org.northshore.cbri.dict.phrase.PhraseDictionaryModel;

class DictionaryResource implements SharedResourceObject {
	
	PhraseDictionaryModel model;

	@Override
	public void load(DataResource aData) throws ResourceInitializationException {

	}

	PhraseDictionaryModel getModel() { return model }
}
