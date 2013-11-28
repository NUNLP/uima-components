package org.northshore.cbri;

import static org.junit.Assert.*

import org.apache.ctakes.typesystem.type.textspan.Segment
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.Test

import com.google.common.base.Charsets
import com.google.common.io.Resources

class SentenceAnnotatorTest {

    AnalysisEngine sentDetector

    @Before
    public void setUp() throws Exception {
        // Make the sentence detector
        //		AnalysisEngineDescription sentDetectorDesc = AnalysisEngineFactory.createEngineDescription(
        //			opennlp.uima.sentdetect.SentenceDetector,
        //			"opennlp.uima.SentenceType", Sentence.name,
        //			"opennlp.uima.ContainerType", Segment.name)
        //		ExternalResourceFactory.createDependencyAndBind(sentDetectorDesc, UimaUtil.MODEL_PARAMETER,
        //				opennlp.uima.sentdetect.SentenceModelResourceImpl, "file:models/sd-med-model.zip")
        AnalysisEngineDescription sentDetectorDesc = AnalysisEngineFactory.createEngineDescription(
                SentenceDetector,
                SentenceDetector.SD_MODEL_FILE_PARAM, "models/sd-med-model.zip");
        sentDetectorDesc.toXML(new FileWriter(new File("src/test/resources/descriptors/SentenceDetector.xml")))
        sentDetector = AnalysisEngineFactory.createEngine(sentDetectorDesc)
        assert sentDetector != null
    }

    @After
    public void tearDown() throws Exception {
        sentDetector = null
    }

    @Test
    public void smokeTest() {
        // load in the text to process
        URL url = Resources.getResource("data/test-note-1.txt")
        String text = Resources.toString(url, Charsets.UTF_8)

        // create a new CAS and seed with a Segment
        JCas jcas = sentDetector.newJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Segment, begin:0, end:text.length())

        // apply the sentence detector
        sentDetector.process(jcas)
        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
        assert sents.size() == 28
        sents.each { println "Sentence: $it.coveredText" }
    }
}
