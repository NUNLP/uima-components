package org.northshore.cbri.brat;

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import groovy.util.logging.Log4j

import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

@Log4j
public class BratDocumentTest {
	static Map<String, String> typeToClassMap = new HashMap<String, String>()

	@BeforeClass
	public static void setupClass() {
		BasicConfigurator.configure()
	}

	@Before
	public void setUp() throws Exception {
		log.setLevel(Level.INFO)
	}

	@Test
	public void testDocumentParsing() throws IOException {
		InputStream annIn = BratDocumentTest.class.getResourceAsStream(
				"/annotated/path-note-1.ann")

		BratDocument doc = BratDocument.parseDocument(annIn)

		Collection<BratAnnotation> anns = doc.getAnnotations()
		assertEquals(12, anns.size())
		
		Map<String, SpanAnnotation> spans = doc.getSpanAnnotations()
		assertEquals(8, spans.size())
		
		Map<String, AttributeAnnotation> attrs = doc.getAttrAnnotations()
		assertEquals(1, attrs.size())
		
		Map<String, RelationAnnotation> rels = doc.getRelAnnotations()
		assertEquals(3, rels.size())
	}
}
