package org.northshore.cbri

import static org.junit.Assert.*
import groovy.util.logging.Log4j
import opennlp.uima.util.UimaUtil

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory
import org.apache.uima.resource.metadata.TypeSystemDescription
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

@Log4j
class AnnotatorTest {

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
    public void smokeTest() {
        // Type system
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // Segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator.class,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, "groovy/SimpleSegmenter.groovy")

        // Sentence detector
        AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SD_MODEL_FILE_PARAM, "models/sd-med-model.zip")

        // Tokenizer
        AnalysisEngineDescription tokenizer = AnalysisEngineFactory.createEngineDescription(
                TokenAnnotator)
        ExternalResourceFactory.createDependencyAndBind(tokenizer,
                TokenAnnotator.TOKEN_MODEL_KEY,
                opennlp.uima.tokenize.TokenizerModelResourceImpl,
                "file:models/en-token.bin")
        
        // POS tagger
        AnalysisEngineDescription posTagger = AnalysisEngineFactory.createEngineDescription(
                opennlp.uima.postag.POSTagger,
                "opennlp.uima.SentenceType", Sentence.name,
                "opennlp.uima.TokenType", BaseToken.name,
                "opennlp.uima.POSFeature", "partOfSpeech")
        ExternalResourceFactory.createDependencyAndBind(posTagger, UimaUtil.MODEL_PARAMETER,
                opennlp.uima.postag.POSModelResourceImpl, "file:models/mayo-pos.zip")
        
        // Lex normalizer
        AnalysisEngineDescription lexNormalizer = AnalysisEngineFactory.createEngineDescription(LexNormalizerAnnotator,
            LexNormalizerAnnotator.PARAM_LEXICON_NAME, "2006Lexicon")

        // Build the aggregate
        AggregateBuilder builder = new AggregateBuilder()
        builder.add(segmenter)
        builder.add(sentDetector)
        builder.add(tokenizer)
        builder.add(posTagger)
        builder.add(lexNormalizer)
        
        AnalysisEngineDescription desc = builder.createAggregateDescription()
        ////PrintWriter writer = new PrintWriter(new File("src/test/resources/descriptors/TestAggregateUIMAFit.xml"))
        ////desc.toXML(writer)
        ////writer.close()

        AnalysisEngine engine = builder.createAggregate()
    }
}
