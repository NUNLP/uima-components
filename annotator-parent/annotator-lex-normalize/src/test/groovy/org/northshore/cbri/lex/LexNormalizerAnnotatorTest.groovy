package org.northshore.cbri.lex

import static org.apache.uima.fit.factory.AnalysisEngineFactory.*
import static org.junit.Assert.*
import gov.nih.nlm.nls.lvg.Api.NormApi
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.syntax.WordToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.fit.factory.JCasFactory
import org.apache.uima.fit.testing.factory.TokenBuilder
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.northshore.cbri.dsl.UIMAUtil

@Log4j
class LexNormalizerAnnotatorTest {

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

    @Ignore
    @Test
    public void testNormApi() {
        NormApi normalizer = new NormApi()
        Vector<String> normsters = normalizer.Mutate("HYPERPLASTIC POLYP, with adenomatous features.")
        assert normsters.size() == 1
        normsters.each { println it }
    }

    @Ignore
    @Test
    public void testLexNormalizerAnnotator() {
        AnalysisEngine engine = createEngine(LexNormalizerAnnotator,
            LexNormalizerAnnotator.PARAM_LEXICON_NAME, "2006Lexicon")
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
            "cooLon":"colon",
			"histerectymy":"hysterectomy"
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
    public void testDKPro() {
        fail("Not yet implemented")
    }
}
