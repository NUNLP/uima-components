package org.northshore.cbri

import static org.northshore.cbri.UIMAUtil.*

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.relation.RelationArgument
import org.apache.ctakes.typesystem.type.relation.UMLSRelation
import org.apache.ctakes.typesystem.type.textsem.EntityMention
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation
import org.apache.ctakes.typesystem.type.textspan.Segment
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.northshore.cbri.UIMAUtil.*

class BratAnnotatorImpl extends BratAnnotator {

	@Override
	protected Annotation mapSpanAnnotation(JCas jcas, SpanAnnotation span) {
		Annotation ann = null

		switch (span.type) {
			case "Final_Diagnosis":
				ann = create(type:Segment, begin:span.span.start, end:span.span.end)
				ann.id = "FINAL_DIAGNOSIS"
				break;
			case "Gross":
				ann = create(type:Segment, begin:span.span.start, end:span.span.end)
				ann.id = "GROSS"
				break;
			case "Rectum":
				ann = create(type:EntityMention,
				begin:span.span.start,
				end:span.span.end,
				polarity :1 , uncertainty :0 , ontologyConcepts :[
					create(type:UmlsConcept, codingScheme:"SNOMED", cui:"C0034896", tui:"T023")
				])
				break;
			case "Sigmoid":
				ann = create(type:EntityMention,
				begin:span.span.start,
				end:span.span.end,
				polarity :1 , uncertainty :0 , ontologyConcepts :[
					create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0227391", tui:"T023")
				])
				break;
			case "Splenic_Flexure":
				ann = create(type:EntityMention,
				begin:span.span.start,
				end:span.span.end,
				polarity :1 , uncertainty :0 , ontologyConcepts :[
					create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0227387", tui:"T023")
				])
				break;
			case "Hepatic_Flexure":
				ann = create(type:EntityMention,
				begin:span.span.start,
				end:span.span.end,
				polarity :1 , uncertainty :0 , ontologyConcepts :[
					create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0227385", tui:"T023")
				])
				break;
			case "Adenoma":
				ann = create(type:EntityMention,
				begin:span.span.start,
				end:span.span.end,
				polarity :1 , uncertainty :0 , ontologyConcepts :[
					create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0850572", tui:"T191")
				])
				break;
			case "Hyperplastic":
				ann = create(type:EntityMention,
				begin:span.span.start,
				end:span.span.end,
				polarity :1 , uncertainty :0 , ontologyConcepts :[
					create(type:UmlsConcept , codingScheme:"SNOMED" , cui:"C0267364", tui:"T191")
				])
				break;
		}

		return ann;
	}

	@Override
	protected UMLSRelation createUMLSRelation(JCas jcas, IdentifiedAnnotation arg1,
		IdentifiedAnnotation arg2, RelationAnnotation rel) {
		UIMAUtil.jcas = jcas
		UMLSRelation umls = null;
		switch (rel.type) {
			case "Finding":
				create(type:UMLSRelation,
				arg1:create(type:RelationArgument, role:"Finding", argument:arg1),
				arg2:create(type:RelationArgument, role:"LocationOf", argument:arg2)
				)
				break;
		}

		return umls;
	}
}
