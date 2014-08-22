package org.northshore.cbri.dict

import groovy.util.logging.Log4j

import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException

@Log4j
class ConceptMapper extends JCasAnnotator_ImplBase {
    public static final String PARAM_DICT_FILE = "dictionaryFile";
    @ConfigurationParameter(name = 'dictionaryFile', mandatory = true, description = 'Configuration parameter key/label for the dictionary file to load')
    private String dictionaryFile;
    
    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext)
    }

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
    }
}
