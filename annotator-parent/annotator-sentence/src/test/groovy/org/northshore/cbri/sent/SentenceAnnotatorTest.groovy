package org.northshore.cbri.sent

import static org.junit.Assert.*
import groovy.util.logging.Log4j
import opennlp.uima.util.UimaUtil

import org.apache.ctakes.typesystem.type.textspan.Segment
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.metadata.TypeSystemDescription
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.northshore.cbri.dsl.GroovyAnnotator
import org.northshore.cbri.dsl.UIMAUtil

import com.google.common.base.Charsets
import com.google.common.io.Resources

@Log4j
class SentenceAnnotatorTest {
    
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
    public void smokeTestOpenNLP() {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                opennlp.uima.sentdetect.SentenceDetector,
                'opennlp.uima.SentenceType', Sentence.name,
                'opennlp.uima.ContainerType', Segment.name)
        ExternalResourceFactory.createDependencyAndBind(desc, UimaUtil.MODEL_PARAMETER,
                opennlp.uima.sentdetect.SentenceModelResourceImpl, 'file:models/sd-med-model.zip')
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        assert engine != null
    }

    @Test
    public void smokeTest() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()
        
        // create a sentence detector engine
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SD_MODEL_FILE_PARAM, 'models/sd-med-model.zip')
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-1.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new CAS and seed with a Segment
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Segment, begin:0, end:text.length())

        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 21
        sents.each { println "Sentence: $it.coveredText" }
    }

    @Test
    public void testWithNoOptions() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/TestSegmenter.groovy')
        
        // sentence detector
        AnalysisEngineDescription sentDetectorDesc = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SD_MODEL_FILE_PARAM, 'models/sd-med-model.zip')
        
        // build the aggregate analysis engine
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetectorDesc)
        }
        AnalysisEngine engine = builder.createAggregate()
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-2.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new JCas
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        
        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 8
        sents.each { println "Sentence: $it.coveredText" }
    }
    
    @Test
    public void testWithSegmentSelected() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/TestSegmenter.groovy')
        
        // sentence detector
        AnalysisEngineDescription sentDetectorDesc = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SD_SEGMENTS_TO_PARSE, 'groovy/TestSegmentsToParse.groovy',
                SentenceDetector.SD_MODEL_FILE_PARAM, 'models/sd-med-model.zip')
        
        // build the aggregate analysis engine
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetectorDesc)
        }
        AnalysisEngine engine = builder.createAggregate()
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-2.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new JCas
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        
        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 6
        sents.each { println "Sentence: $it.coveredText" }
    }

    @Test
    public void testWithSentenceBreakingPattern() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/TestSegmenter.groovy')
        
        // sentence detector
        AnalysisEngineDescription sentDetectorDesc = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SD_SPLIT_PATTERN, ':',
                SentenceDetector.SD_MODEL_FILE_PARAM, 'models/sd-med-model.zip')
        
        // build the aggregate analysis engine
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetectorDesc)
        }
        AnalysisEngine engine = builder.createAggregate()
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-2.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new JCas
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        
        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 13
        sents.each { println "Sentence: $it.coveredText" }
    }

    @Test
    public void testWithNewlineSentenceBreakingPattern() {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/SimpleSegmenter.groovy')
        
        // sentence detector
        AnalysisEngineDescription sentDetectorDesc = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SD_SPLIT_PATTERN, '[\\n\\r:]+',
                SentenceDetector.SD_MODEL_FILE_PARAM, 'models/sd-med-model.zip')
        
        // build the aggregate analysis engine
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetectorDesc)
        }
        AnalysisEngine engine = builder.createAggregate()
        
        // load in the text to process
        URL url = Resources.getResource('data/test-note-1.txt')
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new JCas
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        
        // apply the sentence detector
        engine.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 28
        sents.each { println "Sentence: $it.coveredText" }
    }
    
    /**
     * 
     * @param args
     */
    static void main(args) {
        //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()

        // sentence detector
        AnalysisEngineDescription sentDetectorDesc = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SD_SPLIT_PATTERN, '[\\r\\n:]+',
                SentenceDetector.SD_MODEL_FILE_PARAM, 'models/sd-med-model.zip')

        // segmenter
        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
                tsd, GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/SimpleSegmenter.groovy')

        // build the aggregate
        AggregateBuilder builder = new AggregateBuilder()
        builder.with {
            add(segmenter)
            add(sentDetectorDesc)
        }
        AnalysisEngineDescription desc = builder.createAggregateDescription()
        PrintWriter writer = new PrintWriter(new File('src/test/resources/descriptors/SentenceDetector.xml'))
        desc.toXML(writer)
        writer.close()
    }
}
