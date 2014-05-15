package org.northshore.cbri;

import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.structured.Metadata
import org.apache.ctakes.typesystem.type.structured.SourceData
import org.apache.ctakes.typesystem.type.textsem.TimeMention
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.metadata.TypeSystemDescription
import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

@Log4j
class TimeExRecognizerTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        BasicConfigurator.configure()
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
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
       //build type system description
        TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        tsd.resolveImports()
        
        // create a sentence detector engine
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                TimeExRecognizer,
                TimeExRecognizer.REFERENCE_DATE, '2014-05-12')
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        
        assert engine != null
        
        // load in the text to process
        String text = 'The patient had an onset of diabetes on May 6, 2000 at 3pm. That was 14 years ago.'

        // create a new CAS and seed with a Segment
        JCas jcas = engine.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        
        Metadata metadata = new Metadata(jcas)
        SourceData sourceData = new SourceData(jcas)
        sourceData.setSourceOriginalDate('2014-05-09')
        metadata.sourceData = sourceData
        sourceData.addToIndexes()
        metadata.addToIndexes()

        // apply the sentence detector
        engine.process(jcas)
        Collection<TimeMention> timeMentions = UIMAUtil.select(type:TimeMention)
        assert timeMentions.size() == 2
        timeMentions.each { 
            println "TimeMention: $it.coveredText" 
            println "=>(normalized): ${it.getTime().getNormalizedForm()}"
        }
    }
}
