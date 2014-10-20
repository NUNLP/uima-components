package org.northshore.cbri.dict

import groovy.util.logging.Log4j
import opennlp.tools.sentdetect.SentenceModel

import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.conceptMapper.support.dictionaryResource.DictionaryResource;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.fit.descriptor.ExternalResource
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException

import com.google.common.io.Resources

@Log4j
class ConceptMapper extends JCasAnnotator_ImplBase {
    public static final String PARAM_DICT_FILE = "dictionaryFile";
    @ConfigurationParameter(name = 'dictionaryFile', mandatory = true, description = 'Configuration parameter key/label for the dictionary file to load')
    private String dictionaryFile;
    
    final static String MODEL_KEY = 'Model'
    @ExternalResource(key = 'Model')
    private DictionaryModel dict
    
    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext)
        try {
            ////InputStream modelIn = Resources.getResource(this.dictionaryFile).openStream();
            dict = (DictionaryResource) uimaContext.getResourceObject(PARAM_DICT_FILE);
            if (!dict.isLoaded()) {
              // logger.logInfo("dictionary not yet loaded");
              dict.loadDictionaryContents(uimaContext, logger, tokenAnnotationName,
                      tokenTypeFeatureName, tokenClassFeatureName, tokenizerDescriptor);
              // logger.logInfo( "now is loaded: "+dict.toString() );
              // System.err.println ("NEW DICTIONARY:\n" + dict.toString());
              // debugWrite (dictDebugFile, dict.toString());
            }
        } catch (Exception ace) {
            throw new ResourceInitializationException(ace);
        }
    }

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        println dict.uri
    }
}
