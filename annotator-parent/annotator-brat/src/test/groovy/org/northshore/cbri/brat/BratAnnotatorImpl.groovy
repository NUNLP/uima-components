package org.northshore.cbri.brat

import static org.northshore.cbri.dsl.UIMAUtil.*

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.relation.RelationArgument
import org.apache.ctakes.typesystem.type.relation.UMLSRelation
import org.apache.ctakes.typesystem.type.textsem.EntityMention
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.northshore.cbri.dsl.UIMAUtil

class BratAnnotatorImpl extends BratAnnotator {

    static final String SNOMED = 'SNOMED'
    static final String BODY_STRUCTURE = 'T023'
    static final String NEOPLASTIC_PROCESS = 'T191'
    static final String LEFT_COLON = 'C0227388'
    static final String RIGHT_COLON = 'C1305188'
    static final String COLON = 'C0009368'
    static final String ADENOMA = 'C0850572'
    static final String TUBULAR_ADENOMA = 'C0334292'
    static final String VILLOUS_ADENOMA = 'C0149862'
    static final String SERRATED_ADENOMA = 'C3266124'
    static final String HYPERPLASTIC_POLYP = 'C0267364'
    static final String ADENOCARCINOMA = 'C0001418'

    @Override
    protected List<Annotation> mapSpanAnnotation(JCas jcas, SpanAnnotation span) {
        List<Annotation> anns = []

        switch (span.type) {
            case 'Left':
            case 'Rectum':
            case 'Sigmoid':
            case 'Descending':
            case 'Splenic_Flexure':
                anns.add(create(type:EntityMention,
                begin:span.span.start,
                end:span.span.end,
                polarity:1 , uncertainty:0 , ontologyConcepts:[create(type:UmlsConcept, codingScheme:SNOMED, cui:LEFT_COLON, tui:BODY_STRUCTURE)]))
                break
            case 'Right':
            case 'Hepatic_Flexure':
            case 'Transverse':
            case 'Ascending':
            case 'Ileum':
            case 'Cecum':
                anns.add(create(type:EntityMention,
                begin:span.span.start,
                end:span.span.end,
                polarity:1 , uncertainty:0 , ontologyConcepts:[create(type:UmlsConcept , codingScheme:SNOMED , cui:RIGHT_COLON, tui:BODY_STRUCTURE)]))
                break
            case 'Colon':
                anns.add(create(type:EntityMention,
                begin:span.span.start,
                end:span.span.end,
                polarity:1 , uncertainty:0 , ontologyConcepts:[create(type:UmlsConcept , codingScheme:SNOMED , cui:COLON, tui:BODY_STRUCTURE)]))
                break;
            case 'Adenoma':
                anns.add(create(type:EntityMention,
                begin:span.span.start,
                end:span.span.end,
                polarity:1 , uncertainty:0 , ontologyConcepts:[create(type:UmlsConcept , codingScheme:SNOMED , cui:ADENOMA, tui:NEOPLASTIC_PROCESS)]))
                break
            case 'Tubular':
                anns.add(create(type:EntityMention,
                begin:span.span.start,
                end:span.span.end,
                polarity:1 , uncertainty:0 , ontologyConcepts:[create(type:UmlsConcept , codingScheme:SNOMED , cui:TUBULAR_ADENOMA, tui:NEOPLASTIC_PROCESS)]))
                break
            case 'Tubulovillous':
            case 'Villous':
                anns.add(create(type:EntityMention,
                begin:span.span.start,
                end:span.span.end,
                polarity:1 , uncertainty:0 , ontologyConcepts:[create(type:UmlsConcept , codingScheme:SNOMED , cui:VILLOUS_ADENOMA, tui:NEOPLASTIC_PROCESS)]))
                break
            case 'Serrated':
                anns.add(create(type:EntityMention,
                begin:span.span.start,
                end:span.span.end,
                polarity:1 , uncertainty:0 , ontologyConcepts:[create(type:UmlsConcept , codingScheme:SNOMED , cui:SERRATED_ADENOMA, tui:NEOPLASTIC_PROCESS)]))
                break
            case 'Hyperplastic':
                anns.add(create(type:EntityMention,
                begin:span.span.start,
                end:span.span.end,
                polarity:1 , uncertainty:0 , ontologyConcepts:[create(type:UmlsConcept , codingScheme:SNOMED , cui:HYPERPLASTIC_POLYP, tui:NEOPLASTIC_PROCESS)]))
                break
            case 'Adenocarcinoma':
                anns.add(create(type:EntityMention,
                begin:span.span.start,
                end:span.span.end,
                polarity:1 , uncertainty:0 , ontologyConcepts:[create(type:UmlsConcept , codingScheme:SNOMED , cui:ADENOCARCINOMA, tui:NEOPLASTIC_PROCESS)]))
                break
        }

        return anns
    }

    @Override
    protected UMLSRelation createUMLSRelation(JCas jcas, IdentifiedAnnotation arg1,
        IdentifiedAnnotation arg2, RelationAnnotation rel) {
        UIMAUtil.jcas = jcas
        UMLSRelation umls = null;
        switch (rel.type) {
            case 'Finding':
                create(type:UMLSRelation,
                arg1:create(type:RelationArgument, role:'Finding', argument:arg1),
                arg2:create(type:RelationArgument, role:'LocationOf', argument:arg2)
                )
                break
        }

        umls
    }
}
