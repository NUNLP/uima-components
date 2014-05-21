package org.northshore.cbri;

import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.syntax.WordToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

@Log4j
class TokenAnnotatorTest {

    @BeforeClass
    public static void setupClass() {
        BasicConfigurator.configure()
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testTokenAnnotator() {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                TokenAnnotator,
                TokenAnnotator.PARAM_POST_PROCESS_SCRIPT_FILE, 
                "/groovy/TokenPostProcess.groovy")
        ExternalResourceFactory.createDependencyAndBind(desc, 
            TokenAnnotator.TOKEN_MODEL_KEY,
            opennlp.uima.tokenize.TokenizerModelResourceImpl, 
            "file:models/en-token.bin")
        AnalysisEngine tokenizer = AnalysisEngineFactory.createEngine(desc)
        assert tokenizer != null
        

        // create a new JCas and seed with sentences
        String text = "There was a tubular adenoma in the sigmoid colon."
        JCas jcas = tokenizer.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Sentence, begin:0, end:text.length())

        // apply the tokenizer
        tokenizer.process(jcas)
        
        // verify number of BaseTokens
        Collection<BaseToken> baseTokens = UIMAUtil.select(type:BaseToken)
        assert baseTokens.size() == 10

        // verify number of WordTokens
        Collection<WordToken> wordTokens = UIMAUtil.select(type:WordToken)
        assert wordTokens.size() == 9        
    }
}
