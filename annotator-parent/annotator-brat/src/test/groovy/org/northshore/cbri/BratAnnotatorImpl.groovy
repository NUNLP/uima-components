package org.northshore.cbri

import static org.northshore.cbri.UIMAUtil.*

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.textsem.EntityMention
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
			case "Rectum":
				EntityMention mention = create(type:EntityMention,
					begin:span.span.start,
					end:span.span.end,
					polarity :1 , uncertainty :0 , ontologyConcepts :[
						create(type:UmlsConcept, codingScheme:"SNOMED", cui:"C0034896", tui:"T023")
					])
				break;
			case "Sigmoid":
				EntityMention mention = create(type:EntityMention, 
					begin:span.span.start,
					end:span.span.end,
					polarity :1 , uncertainty :0 , ontologyConcepts :[
						create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0227391", tui:"T023")
					])
				break;
			case "Splenic_Flexure":
				EntityMention mention = create(type:EntityMention,
					begin:span.span.start,
					end:span.span.end,
					polarity :1 , uncertainty :0 , ontologyConcepts :[
						create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0227387", tui:"T023")
					])
				break;
			case "Hepatic_Flexure":
				EntityMention mention = create(type:EntityMention,
					begin:span.span.start,
					end:span.span.end,
					polarity :1 , uncertainty :0 , ontologyConcepts :[
						create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0227385", tui:"T023")
					])
				break;
			case "Adenoma":
				EntityMention mention = create(type:EntityMention,
					begin:span.span.start,
					end:span.span.end,
					polarity :1 , uncertainty :0 , ontologyConcepts :[
						create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0850572", tui:"T191")
					])
				break;
			case "Hyperplastic":
				EntityMention mention = create(type:EntityMention,
					begin:span.span.start,
					end:span.span.end,
					polarity :1 , uncertainty :0 , ontologyConcepts :[
						create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0267364", tui:"T191")
					])
				break;
		}

		return ann;
	}
}
