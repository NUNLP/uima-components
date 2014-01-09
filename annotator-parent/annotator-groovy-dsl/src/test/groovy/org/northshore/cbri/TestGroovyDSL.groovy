package org.northshore.cbri;

import static org.northshore.cbri.UIMAUtil.*
import static org.junit.Assert.*

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.textsem.EntityMention
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.UIMAFramework
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.DocumentAnnotation
import org.apache.uima.resource.metadata.TypeSystemDescription
import org.apache.uima.util.InvalidXMLException
import org.apache.uima.util.XMLInputSource
import org.junit.Before
import org.junit.Test
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory
import org.apache.uima.fit.pipeline.SimplePipeline

import org.northshore.cbri.AnnotationMatcher
import org.northshore.cbri.GroovyAnnotator

class TestGroovyDSL {
	static AnalysisEngine engine;
	static JCas jcas;
	
	// TODO: replace with manual creation of annotations, to avoid using code that is meant
	// to be tested in the first place
	static {
		def tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
		AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(
				GroovyAnnotator,
				GroovyAnnotator.PARAM_SCRIPT_FILE, "groovy/TestSegmenter.groovy")
		AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
				GroovyAnnotator,
				GroovyAnnotator.PARAM_SCRIPT_FILE, "groovy/TestSentenceDetector.groovy")
		AnalysisEngineDescription conceptDetector = AnalysisEngineFactory.createEngineDescription(
				GroovyAnnotator,
				GroovyAnnotator.PARAM_SCRIPT_FILE, "groovy/TestConceptDetector.groovy")

		AggregateBuilder builder = new AggregateBuilder()
		builder.add(segmenter)
		builder.add(sentDetector)
		builder.add(conceptDetector)
		engine = builder.createAggregate()
		jcas = engine.newJCas()
	}

	@Before
	void setUp() {
		jcas.reset()
	}

	@Test
	public void testSelect() {
		jcas.setDocumentText(
				"""\
        Patient has fever but no cough and pneumonia is ruled out.
        There is no increase in weakness.
        Patient does not have measles.
        """
				);
		SimplePipeline.runPipeline(this.jcas, this.engine)
		
		assert select(type:EntityMention).size() == 4

		assert select(type:Sentence,
		filter:not(contains(EntityMention))
		).size() == 1

		def sentsWithMentions = select(type:Sentence,
		filter:contains(EntityMention)
		)
		assert sentsWithMentions.size() == 2

		assert select(type:EntityMention,
		filter:coveredBy(sentsWithMentions[0])
		).size() == 3

		assert select(type:EntityMention,
		filter:not(coveredBy(sentsWithMentions[0]))
		).size() == 1

		assert select(type:EntityMention,
		filter:between(0, 60)
		).size() == 3

		assert select(type:EntityMention,
		filter:before(60)
		).size() == 3

		assert select(type:EntityMention,
		filter:after(60)
		).size() == 1
	}

	@Test
	public void testCreate() {
		int startIndex = 0; int endIndex = 10
		EntityMention em = create(type:EntityMention,
		begin:startIndex, end:endIndex,
		polarity:1, uncertainty:0,
		ontologyConcepts:[
			create(type:UmlsConcept, codingScheme:"SNOMED", code:1, cui:"C01", tui:"T01"),
			create(type:UmlsConcept, codingScheme:"SNOMED", code:2, cui:"C02", tui:"T02")
		]
		)

		assert em != null
		assert em.ontologyConcepts.size() == 2
	}

	@Test
	public void testRegexMatch() {
		jcas.setDocumentText(
				"""\
Patient has fever but no cough and pneumonia is ruled out.
The patient does not have pneumonia or sepsis.
        """
				);
		SimplePipeline.runPipeline(this.jcas, this.engine)

		Collection sents = select(type:Sentence)
		assert sents.size() == 2
		Sentence sent1 = sents[0]
		Sentence sent2 = sents[1]

		AnnotationMatcher m = (~/(?<d1>pneumonia) or (?<d2>sepsis)/).matcher(coveringAnn:sent2)
		Iterator iter = m.iterator()
		assert iter.hasNext()
		iter.next()
		assert m.group(1) != null
		assertEquals "pneumonia", m.group(1)
		assertEquals 85, m.start(1)
		assertEquals 85 + ('pneumonia').length(),  m.end(1)
		assert m.group(2) != null
		assertEquals "sepsis", m.group(2)
		assertEquals 98, m.start(2)
		assertEquals 98 + ('sepsis').length(), m.end(2)

		m = (~/(cough|fever)/).matcher(coveringAnn:sent1)
		iter = m.iterator()
		assert iter.hasNext()
		iter.next()
		assert m.group(1) != null
		assertEquals "fever", m.group(1)
		assertEquals 12, m.start(1)
		assertEquals 12 + ('fever').length(), m.end(1)
		assert iter.hasNext()
		iter.next()
		assertEquals "cough", m.group(1)
		assertEquals 25, m.start(1)
		assertEquals 25 + ('cough').length(), m.end(1)

		// use "each" idiom
		m = (~/(cough|fever)/).matcher(coveringAnn:sent1)
		m.each{
			assert m.group(1) in ['fever', 'cough']
		}
	}

	@Test
	public void testAnnRegexMatch() {
		jcas.setDocumentText("Patient has fever but no cough, pneumonia and sepsis are ruled out.");
		SimplePipeline.runPipeline(jcas, engine)
		DocumentAnnotation docAnn = jcas.documentAnnotationFs

		AnnotationMatcher m = (~/(?<E1>@EntityMention) but no (?<E2>@EntityMention)/).matcher(
				coveringAnn:docAnn, types:[EntityMention], includeText:true)
		Iterator iter = m.iterator()
		assert iter.hasNext()
		def Map binding = iter.next()
		assert binding.get("E1").coveredText == "fever"
		assert binding.get("E2").coveredText == "cough"
		assert !iter.hasNext()

		m = (~/(?<E1>@EntityMention) and (?<E2>@EntityMention).+ruled out/).matcher(
				coveringAnn:docAnn, types:[EntityMention], includeText:true)
		iter = m.iterator()
		assert iter.hasNext()
		binding = iter.next()
		assert binding.get("E1").coveredText == "pneumonia"
		assert binding.get("E2").coveredText == "sepsis"
		assert !iter.hasNext()

		m = (~/(?<E1>@EntityMention)(?<E2>@EntityMention)/).matcher(
				coveringAnn:docAnn, types:[EntityMention], includeText:false)
		iter = m.iterator()
		assert iter.hasNext()
		binding = iter.next()
		assert binding.get("E1").coveredText == "fever"
		assert binding.get("E2").coveredText == "cough"
		assert iter.hasNext()
		binding = iter.next()
		assert binding.get("E1").coveredText == "pneumonia"
		assert binding.get("E2").coveredText == "sepsis"
		assert !iter.hasNext()

		// test non-Annotation groups (vanilla capturing groups not handled yet)
		m = (~/(?<chunk1>(?<E1>@EntityMention)(?<t> and ))(?<chunk2>(?<E2>@EntityMention).+(?:ruled out))/).matcher(
				coveringAnn:docAnn, types:[EntityMention], includeText:true)
		iter = m.iterator()
		assert iter.hasNext()
		binding = iter.next()
		assert m.matcher.group("t") != null
		assertEquals " and ", m.matcher.group("t")
		assert m.matcher.group("chunk1") != null
		assertEquals "@EntityMention and ", m.matcher.group("chunk1")
		assert m.matcher.group("chunk2") != null
		assertEquals "@EntityMention are ruled out", m.matcher.group("chunk2")
		assert binding.get("E1") != null
		assert binding.get("E1").coveredText == "pneumonia"
		assert binding.get("E2") != null
		assert binding.get("E2").coveredText == "sepsis"
		assert !iter.hasNext()
	}
}
