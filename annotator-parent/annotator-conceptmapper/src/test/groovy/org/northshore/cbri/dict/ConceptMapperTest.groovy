package org.northshore.cbri.dict;

import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.UIMAFramework
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.conceptMapper.DictTerm
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.util.XMLInputSource
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.northshore.cbri.dsl.UIMAUtil

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
        JCas jcas = engine.newJCas()
        jcas.setDocumentText('Papillary squamous cell carcinoma in situ.')
        engine.process(jcas)
        UIMAUtil.jcas = jcas
        Collection<DictTerm> terms = UIMAUtil.select(type:DictTerm)
        terms.each { println "DictTerm: ${it.coveredText}: [${it.attributeType}, ${it.attributeValue}]" }
        assert terms.size() == 1
    }

    @Ignore
    @Test
    public void createEngineUIMAfit() {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                org.northshore.cbri.conceptmapper.ConceptMapper)
        assert desc != null
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        assert engine != null
    }
}
