package org.northshore.cbri

import static org.apache.uima.fit.util.CasUtil.getType
import static org.apache.uima.fit.util.JCasUtil.select
import static org.apache.uima.fit.util.JCasUtil.selectCovered
import groovy.json.JsonSlurper
import groovy.util.logging.Log4j

import java.lang.reflect.Type

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.tools.util.Span
import opennlp.uima.tokenize.TokenizerModelResource

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.fit.descriptor.ExternalResource
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceAccessException
import org.apache.uima.resource.ResourceInitializationException

import de.tudarmstadt.ukp.dkpro.core.dictionaryannotator.PhraseTree

@Log4j
public class UmlsDictionaryAnnotator_LingPipe extends JCasAnnotator_ImplBase {
    
    @ExternalResource(key = 'token_model')
    TokenizerModelResource modelResource

    public static final String PARAM_DICTIONARY_FILE = 'dictFile'
    @ConfigurationParameter(name = 'dictFile', mandatory = true)
    private String dictFile
    
    public static final String PARAM_DICTIONARY_FILE_ENCODING = 'dictFileEncoding'
    @ConfigurationParameter(name = 'dictFileEncoding', mandatory = true, defaultValue='UTF-8')
    private String dictFileEncoding
    
    private PhraseTree phrases
    private Map<List<String>, Map<String, String>> phraseSems

    @Override
    public void initialize(UimaContext aContext)
    throws ResourceInitializationException {
        super.initialize(aContext)
    }

    @Override
    public void process(JCas jcas)
            throws AnalysisEngineProcessException
    {
    }
}
