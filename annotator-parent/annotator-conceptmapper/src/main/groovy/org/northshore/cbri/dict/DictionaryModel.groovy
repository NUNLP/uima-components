package org.northshore.cbri.dict

import org.apache.uima.resource.DataResource
import org.apache.uima.resource.ResourceInitializationException
import org.apache.uima.resource.SharedResourceObject

final class DictionaryModel implements SharedResourceObject {
    private String uri;

    public void load(DataResource aData)
    throws ResourceInitializationException {

        uri = aData.getUri().toString();
    }

    public String getUri() {
        return uri;
    }
}
