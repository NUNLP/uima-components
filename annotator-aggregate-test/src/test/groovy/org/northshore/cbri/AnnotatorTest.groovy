package org.northshore.cbri

import static org.junit.Assert.*
import opennlp.uima.util.UimaUtil

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory
import org.apache.uima.resource.metadata.TypeSystemDescription
import org.junit.After
import org.junit.Before
import org.junit.Test

class AnnotatorTest {
    
    AnalysisEngine engine = null;

    @Before
    public void setUp() throws Exception {
        // Build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // Sentence detector
        AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SD_MODEL_FILE_PARAM, "models/sd-med-model.zip")

        // Tokenizer
        AnalysisEngineDescription tokenizer = AnalysisEngineFactory.createEngineDescription(
                opennlp.uima.tokenize.Tokenizer,
                "opennlp.uima.SentenceType", Sentence.name,
                "opennlp.uima.TokenType", BaseToken.name)
        ExternalResourceFactory.createDependencyAndBind(tokenizer, UimaUtil.MODEL_PARAMETER,
                opennlp.uima.tokenize.TokenizerModelResourceImpl, "file:models/en-token.bin")

        // POS tagger
        AnalysisEngineDescription posTagger = AnalysisEngineFactory.createEngineDescription(
                opennlp.uima.postag.POSTagger,
                "opennlp.uima.SentenceType", Sentence.name,
                "opennlp.uima.TokenType", BaseToken.name,
                "opennlp.uima.POSFeature", "partOfSpeech")
        ExternalResourceFactory.createDependencyAndBind(posTagger, UimaUtil.MODEL_PARAMETER,
                opennlp.uima.postag.POSModelResourceImpl, "file:models/mayo-pos.zip")
        
        // Segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator.class,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, "groovy/SimpleSegmenter.groovy")

        // Build the aggregate
        AggregateBuilder builder = new AggregateBuilder()
        builder.add(segmenter)
        builder.add(sentDetector)
        builder.add(tokenizer)
        builder.add(posTagger)
        
        AnalysisEngineDescription desc = builder.createAggregateDescription()
        PrintWriter writer = new PrintWriter(new File("src/test/resources/descriptors/TestAggregateUIMAFit.xml"))
        desc.toXML(writer)
        writer.close()

        this.engine = builder.createAggregate()
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        fail("Not yet implemented")
    }
}
