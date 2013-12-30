package org.northshore.cbri;

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue
import groovy.util.logging.Log4j

import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.google.common.base.Charsets
import com.google.common.io.Resources

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
		URL url = Resources.getResource("annotated/path-note-1.txt");
		String text = Resources.toString(url, Charsets.UTF_8);

		InputStream annIn = BratDocumentTest.class.getResourceAsStream(
				"/annotated/path-note-1.ann")

		BratDocument doc = BratDocument.parseDocument(annIn)

		assertTrue(text.startsWith("CASE: XXX-00-00000"))
		assertTrue(text.endsWith("Sep 22, 2222  22:22:22\n"))
		assertEquals(995, text.length())

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
