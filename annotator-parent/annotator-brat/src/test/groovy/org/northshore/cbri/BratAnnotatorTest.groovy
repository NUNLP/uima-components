package org.northshore.cbri;

import static org.junit.Assert.*
import static org.northshore.cbri.UIMAUtil.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.relation.UMLSRelation
import org.apache.ctakes.typesystem.type.textsem.EntityMention
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
	
	public static final String GOLD_VIEW = "gold"
	
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
		// test note
		URL url = Resources.getResource("annotated/path-note-1.txt")
		String text = Resources.toString(url, Charsets.UTF_8)

		// build a BratAnnotator analysis engine
		def tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
		AnalysisEngineDescription brat = AnalysisEngineFactory.createEngineDescription(
				BratAnnotatorImpl,
				BratAnnotator.PARAM_ANN_FILE, "/annotated/path-note-1.ann",
				BratAnnotator.PARAM_VIEW_NAME, GOLD_VIEW)
		AggregateBuilder builder = new AggregateBuilder()
		builder.add(brat)
		AnalysisEngineDescription desc = builder.createAggregateDescription()
		PrintWriter writer = new PrintWriter(new File("src/test/resources/descriptors/TestBratUIMAFit.xml"))
		desc.toXML(writer)
		writer.close()
		AnalysisEngine engine = builder.createAggregate()
		
		// make the JCas and process
		JCas jcas = engine.newJCas()
		jcas.setDocumentText(text)		
		engine.process(jcas)
		
		// test the result
		Collection<Segment> segs = select(type:Segment)
		assert segs.size() == 1
		Segment seg = segs.find{ it.id == "FINAL_DIAGNOSIS" }
		assertEquals("FINAL DIAGNOSIS:", seg.coveredText)
		
		Collection<EntityMention> mentions = select(type:EntityMention)
		assert mentions.size() == 6
		
		Collection<UMLSRelation> rels = select(type:UMLSRelation)
		assert rels.size() == 3
	}
    
    @Test
    public void pathNoteWithAttribute() {
        // build a BratAnnotator analysis engine
        def tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
        AnalysisEngineDescription brat = AnalysisEngineFactory.createEngineDescription(
                BratAnnotatorImpl,
                BratAnnotator.PARAM_ANN_FILE, "/annotated/path-note-2.ann",
                BratAnnotator.PARAM_VIEW_NAME, GOLD_VIEW)
        AggregateBuilder builder = new AggregateBuilder()
        builder.add(brat)
        AnalysisEngineDescription desc = builder.createAggregateDescription()
        PrintWriter writer = new PrintWriter(new File("src/test/resources/descriptors/TestBratUIMAFit.xml"))
        desc.toXML(writer)
        writer.close()
        AnalysisEngine engine = builder.createAggregate()
        
        // make the JCas and process
        JCas jcas = engine.newJCas()
        String text = 'foo' * 500
        jcas.setDocumentText(text)
        engine.process(jcas)
        
        // test the result
        Collection<Segment> segs = select(type:Segment)
        assert segs.size() == 1
        Segment seg = segs.find{ it.id == "FINAL_DIAGNOSIS" }
        
        Collection<EntityMention> mentions = select(type:EntityMention)
        assert mentions.size() == 2
        
        Collection<UMLSRelation> rels = select(type:UMLSRelation)
        assert rels.size() == 1
    }
}
