package org.northshore.cbri

import static org.northshore.cbri.UIMAUtil.*
import org.apache.ctakes.typesystem.type.textspan.Segment
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.northshore.cbri.UIMAUtil.*

class BratAnnotatorImpl extends BratAnnotator {

	@Override
	protected Annotation mapSpanAnnotation(JCas jcas, SpanAnnotation span) {
		UIMAUtil.jcas = jcas
		Annotation ann = null;
		switch (span.type) {
			case "Final_Diagnosis":
				Segment seg = create(type:Segment, begin:span.span.start, end:span.span.end)
				seg.id = "FINAL_DIAGNOSIS"
				break;
			case "Gross":
				Segment seg = create(type:Segment, begin:span.span.start, end:span.span.end)
				seg.id = "GROSS"
				break;
		}

		return ann;
	}
}
