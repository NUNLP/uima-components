package org.northshore.cbri

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

class POSAnnotatorTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testOpenNLPSentenceAnnotator() {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                opennlp.uima.postag.POSTagger,
                "opennlp.uima.SentenceType", Sentence.name,
                "opennlp.uima.TokenType", BaseToken.name,
                "opennlp.uima.POSFeature", "partOfSpeech")
        ExternalResourceFactory.createDependencyAndBind(desc, UimaUtil.MODEL_PARAMETER,
                opennlp.uima.postag.POSModelResourceImpl, "file:models/mayo-pos.zip")
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        assert engine != null
        
        // load in the text to process
        URL url = Resources.getResource("data/test-note-1.txt")
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new CAS and seed with a Segment
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Segment, begin:0, end:text.length())

//        // apply the sentence detector
//        sentDetector.process(jcas)
//        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
//        assert sents.size() == 28
//        sents.each { println "Sentence: \"$it.coveredText\"" }
    }
}
