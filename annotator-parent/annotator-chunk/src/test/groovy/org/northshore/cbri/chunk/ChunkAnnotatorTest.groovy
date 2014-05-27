package org.northshore.cbri.chunk

import static org.junit.Assert.*

import java.util.regex.Matcher

import opennlp.uima.util.UimaUtil

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.syntax.Chunk
import org.apache.ctakes.typesystem.type.textspan.Segment
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.pipeline.SimplePipeline
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.northshore.cbri.dsl.UIMAUtil;

import com.google.common.base.Charsets
import com.google.common.io.Resources

class ChunkAnnotatorTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testOpenNLPChunkerAnnotator() {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                opennlp.uima.chunker.Chunker,
                "opennlp.uima.SentenceType", Sentence.name,
                "opennlp.uima.TokenType", BaseToken.name,
                "opennlp.uima.POSFeature", "partOfSpeech",
                "opennlp.uima.ChunkType", Chunk,
                "opennlp.uima.ChunkTagFeature", "chunkType")
        ExternalResourceFactory.createDependencyAndBind(desc, UimaUtil.MODEL_PARAMETER,
                opennlp.uima.chunker.ChunkerModelResourceImpl, "file:models/chunker-model.zip")
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        assert engine != null

        // load in the text to process
        URL url = Resources.getResource("data/test-note-1.txt")
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new CAS and seed with tokens
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Segment, begin:0, end:text.length())
        UIMAUtil.create(type:Sentence, begin:0, end:text.length())
        Matcher m = (text =~ /\b\w+\b/)
        m.each {
            UIMAUtil.create(type:BaseToken, begin:m.start(0), end:m.end(0))
        }
        Map<String, String> tokenToPos = new HashMap<>()
        tokenToPos["Tubular"] = "JJ"
        tokenToPos["adenoma"] = "NN"
        tokenToPos["in"] = "IN"
        tokenToPos["the"] = "DT"
        tokenToPos["sigmoid"] = "JJ"
        tokenToPos["colon"] = "NN"
        UIMAUtil.select(type:BaseToken).each { BaseToken token ->
            token.setPartOfSpeech(tokenToPos[token.coveredText])
        }

        // run the Chunker
        SimplePipeline.runPipeline(jcas, engine)
        
        Map<String, String> chunkToType = new HashMap<>()
        chunkToType["Tubular adenoma"] = "NP"
        chunkToType["in"] = "PP"
        chunkToType["the sigmoid colon"] = "NP"
        
        UIMAUtil.select(type:Chunk).each { Chunk chunk ->
            println "[$chunk.chunkType]: $chunk.coveredText"
            assertEquals chunkToType[chunk.coveredText], chunk.getChunkType()
        }
    }
}
