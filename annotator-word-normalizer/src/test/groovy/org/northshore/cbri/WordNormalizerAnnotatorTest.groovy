package org.northshore.cbri

import static org.apache.uima.fit.factory.AnalysisEngineFactory.*
import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.syntax.WordToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.UIMAException
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.JCasFactory
import org.apache.uima.fit.pipeline.SimplePipeline
import org.apache.uima.fit.testing.factory.TokenBuilder
import org.apache.uima.fit.util.JCasUtil
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test

@Log4j
class WordNormalizerAnnotatorTest {

    @BeforeClass
    public static void setupClass() {
        BasicConfigurator.configure()
    }

    @Before
    public void setUp() throws Exception {
        log.setLevel(Level.INFO)
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testNormalizer() {
        AnalysisEngine engine = createEngine(WordNormalizerAnnotator,
            WordNormalizerAnnotator.PARAM_LEXICON_NAME, "2006Lexicon")
        JCas jcas = JCasFactory.createJCas()
        UIMAUtil.jcas = jcas

        TokenBuilder<WordToken, Sentence> tb = new TokenBuilder<>(WordToken, Sentence)
        tb.buildTokens(jcas, "THERE is a Tubular ADEnOeMA in the SigmoiDp cooLon .")
        engine.process(jcas)

        Map<String, String> normMap = [
            "THERE":"there",
            "is":null, 
            "a":"a",
            "Tubular":"tubular",
            "ADEnOeMA":"adenoma",
            "in":null,
            "the":null,
            "SigmoiDp":"sigmoid",
            "cooLon":"colon"
            ]
        Collection<BaseToken> tokens = UIMAUtil.select(type:WordToken)
        assertEquals(10, tokens.size())
        tokens.each { WordToken token ->
            log.info("Word: $token.coveredText; Spell: $token.suggestion; Norm: $token.normalizedForm")
            assertEquals(normMap[token.coveredText], token.normalizedForm)
        }
    }


    @Ignore
    @Test
    public void smokeTest() throws UIMAException, IOException {

        AnalysisEngine ae = AnalysisEngineFactory.createPrimitive(spellCorrector)
        assertNotNull(ae)

        JCas jCas = JCasFactory.createJCas()
        String text = "pnemonia"
        jCas.setDocumentText(text)
        WordToken word = new WordToken(jCas)
        word.setBegin(0)
        word.setEnd(text.length()-1)
        word.addToIndexes()

        SimplePipeline.runPipeline(jCas, ae)

        Collection<WordToken> wordTokens = JCasUtil.select(jCas, WordToken.class)
        assertTrue(wordTokens.size() == 1)
        for (WordToken wt : wordTokens) {
            assertEquals("pneumonia", wt.getSuggestion())
        }
    }

    @Ignore
    @Test
    public void testDKPro() {
        fail("Not yet implemented")
    }
}
