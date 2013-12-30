package org.northshore.cbri;

import static org.junit.Assert.*
import static org.northshore.cbri.UIMAUtil.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.textspan.Segment
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory
import org.apache.uima.jcas.JCas
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.google.common.base.Charsets
import com.google.common.io.Resources

@Log4j
class BratAnnotatorTest {
	@BeforeClass
	public static void setupClass() {
		BasicConfigurator.configure()
	}

	@Before
	public void setUp() throws Exception {
		log.setLevel(Level.INFO)
	}

	@Test
	public void smokeTest() {
		def tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
		AnalysisEngineDescription brat = AnalysisEngineFactory.createEngineDescription(
				BratAnnotatorImpl,
				BratAnnotatorImpl.PARAM_ANN_FILE, "/annotated/path-note-1.ann")
		
		AggregateBuilder builder = new AggregateBuilder()
		builder.add(brat)
		AnalysisEngine engine = builder.createAggregate()
		
		AnalysisEngineDescription desc = builder.createAggregateDescription()
		PrintWriter writer = new PrintWriter(new File("src/test/resources/descriptors/TestBratUIMAFit.xml"))
		desc.toXML(writer)
		writer.close()
		
		JCas jcas = engine.newJCas()
		UIMAUtil.jcas = jcas
		
		URL url = Resources.getResource("annotated/path-note-1.txt")
		String text = Resources.toString(url, Charsets.UTF_8)
		jcas.setDocumentText(text)
		
		engine.process(jcas)
		
		Collection<Segment> segs = select(type:Segment)
		assertEquals(2, segs.size())
		Segment seg = segs.find{ it.id == "FINAL_DIAGNOSIS" }
		assertEquals("FINAL DIAGNOSIS:", seg.coveredText)
		seg = segs.find{ it.id == "GROSS" }
		assertEquals("GROSS:", seg.coveredText)
	}
}
