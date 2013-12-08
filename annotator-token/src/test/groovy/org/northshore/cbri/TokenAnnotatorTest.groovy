package org.northshore.cbri;

import static org.junit.Assert.*
import opennlp.uima.util.UimaUtil

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textspan.Segment
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.Test

import com.google.common.base.Charsets
import com.google.common.io.Resources

class TokenAnnotatorTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testOpenNLPTokenAnnotator() {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                opennlp.uima.tokenize.Tokenizer,
                "opennlp.uima.SentenceType", Sentence.name,
                "opennlp.uima.TokenType", BaseToken.name)
        ExternalResourceFactory.createDependencyAndBind(desc, UimaUtil.MODEL_PARAMETER,
                opennlp.uima.tokenize.TokenizerModelResourceImpl, "file:models/en-token.bin")
        AnalysisEngine tokenizer = AnalysisEngineFactory.createEngine(desc)
        assert tokenizer != null
        
        // load in the text to process
        URL url = Resources.getResource("data/test-note-1.txt")
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new CAS and seed with sentences
        JCas jcas = tokenizer.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Sentence, begin:0, end:text.length())

        // apply the tokenizer
        tokenizer.process(jcas)
        Collection<BaseToken> tokens = UIMAUtil.select(type:BaseToken)
        assert tokens.size() == 239
        
        tokens.each {
            println " '$it.coveredText' "
        }
    }
}
