package org.northshore.cbri;

import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.UIMAFramework
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.resource.ResourceInitializationException
import org.apache.uima.util.InvalidXMLException
import org.apache.uima.util.XMLInputSource
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

@Log4j
class ConceptMapperTest {
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
    public void createEngineDescFile() {
        AnalysisEngineDescription desc = UIMAFramework
                .getXMLParser()
                .parseAnalysisEngineDescription(
                new XMLInputSource(
                ConceptMapperTest.class
                .getResource('/descriptors/aggregate/OffsetTokenizerMatcher.xml')))
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        assert engine != null
    }

    @Test
    public void createEngineUIMAfit() {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                org.apache.uima.conceptMapper.ConceptMapper)
        assert desc != null
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        assert engine != null
    }
}
