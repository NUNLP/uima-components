package org.northshore.cbri

import static org.apache.uima.fit.util.JCasUtil.*

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.ctakes.typesystem.type.refsem.*
import org.apache.ctakes.typesystem.type.relation.*
import org.apache.ctakes.typesystem.type.structured.*
import org.apache.ctakes.typesystem.type.syntax.*
import org.apache.ctakes.typesystem.type.textsem.*
import org.apache.ctakes.typesystem.type.textspan.*
import org.apache.ctakes.typesystem.type.util.*
import org.apache.uima.cas.text.AnnotationFS
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import org.apache.uima.jcas.cas.TOP
import org.apache.uima.jcas.tcas.Annotation

/**
 * UIMAUtil implements the UIMA DSL
 * @author Will Thompson
 * TODO: add these methods to JCas interface
 */
class UIMAUtil extends Script {
    @Override
    Object run() {
        super.run()
    }

    // ------------------------------------------------------------------------
    // Setup
    // ------------------------------------------------------------------------

    // the JCas instance to be used for annotation creation and selection
    static JCas jcas

    // static initialization
    static {
        IdentifiedAnnotation.metaClass.getOntologyConcepts = {
            return (delegate.ontologyConceptArr == null ? []:
            select(delegate.ontologyConceptArr, OntologyConcept))
        }
        IdentifiedAnnotation.metaClass.setOntologyConcepts = { concepts ->
            FSArray array = new FSArray(jcas, concepts.size())
            int i = 0
            concepts.each {
                array.set(i, it)
                i += 1
            }
            delegate.ontologyConceptArr = array
        }
        
        // TODO: move this into the AnnotationMatcher class        
        Matcher.metaClass.start = { String groupName ->
            Integer groupNum = delegate.namedGroupIndex[groupName]
            return (groupNum ? delegate.start(groupNum): null)
        }
        
        // TODO: move this into the AnnotationMatcher class (?)
        Matcher.metaClass.end = { String groupName ->
            Integer groupNum = delegate.namedGroupIndex[groupName]
            return (groupNum ? delegate.end(groupNum): null)
        }
        
        // TODO: move this into the AnnotationMatcher class (?)
        Pattern.metaClass.matcher = { CharSequence input ->
            Matcher retval = input =~ delegate
            
            Pattern metpat = ~/\((\?(<(\w+?)>))/
            Matcher m = delegate.toString() =~ metpat
            Matcher.metaClass.namedGroupIndex = [:]
            int groupCnt = 0
            m.each {
                groupCnt += 1
                if (m.group(3) != null) {
                    ////println "Meta-match: (" + m.group(3) + ") from: " + m.start(3) + " to " + m.end(3)
                    retval.namedGroupIndex[m.group(3)] = groupCnt
                }
            }
            return retval
        }
        
        Pattern.metaClass.matcher = { Map args ->
            Annotation coveringAnn = (args.coveringAnn ?: jcas.documentAnnotationFs)
            Boolean includeText = (args.includeText == false ? false : true)
            java.util.List<Class<Annotation>> types = (args.types ?: [])
            return new AnnotationMatcher(jcas, coveringAnn, types, delegate, includeText)
        }
    }

    /**
     * Set the JCas to use
     * @param jcas
     */
    static void setJCas(JCas jcas) {
        UIMAUtil.jcas = jcas
    }
    
    /**
     * 
     * @param typeName
     * @return
     */
    static Class getIdentifiedAnnotationClass(String typeName) {
        Class typeClass = null
        switch (typeName) {
            case 'AnatomicalSiteMention':
                typeClass = AnatomicalSiteMention
                break
            case 'DiseaseDisorderMention':
                typeClass = DiseaseDisorderMention
                break
            case 'EventMention':
                typeClass = EventMention
                break
            case 'FractionAnnotation':
                typeClass = FractionAnnotation
                break
            case 'LabMention':
                typeClass = LabMention
                break
            case 'MeasurementAnnotation':
                typeClass = MeasurementAnnotation
                break
            case 'MedicationMention':
                typeClass = MedicationMention
                break
            case 'PersonTitleAnnotation':
                typeClass = PersonTitleAnnotation
                break
            case 'ProcedureMention':
                typeClass = ProcedureMention
                break
            case 'RangeAnnotation':
                typeClass = RangeAnnotation
                break
            case 'SignSymptomMention':
                typeClass = SignSymptomMention
                break
            case 'TimeMention':
                typeClass = TimeMention
                break
            case 'IdentifiedAnnotation':
                typeClass = IdentifiedAnnotation
                break
            default:
                throw new IllegalArgumentException("Invalid type in dictionary entry: $typeName")
        }
        return typeClass
    }

    // ------------------------------------------------------------------------
    // Annotation creation functions
    // ------------------------------------------------------------------------

    /**
     * Create UIMA annotation from set of attributes
     * @param attrs
     * @return
     */
    static TOP create(Map attrs) {
        TOP a = attrs.type.newInstance(jcas)
        attrs.each { k, v ->
            if (a.metaClass.hasProperty(a, k)) {
                if (k != 'type') {
                    a."${k}" = v
                }
            }
        }
        a.addToIndexes()
        return a
    }
    
    /**
     * Apply a set of regex patterns to a collection of annotations. For each match, apply
     * the specified closure
     */
    static applyPatterns = { Collection<Annotation> anns, java.util.List<Pattern> patterns, Closure action ->
        anns.each { ann ->
            patterns.each { p ->
                AnnotationMatcher m = p.matcher(coveringAnn:ann); m.each {
                    action.call(m)
                }
            }
        }
    }
    
    /**
     * Create IdentifiedAnnotations from regex matches
     * @param args
     * @return
     */
    static java.util.List<IdentifiedAnnotation> createMentions(Map args) {
        Map patterns = args.patterns
        Collection searchSet = args.searchSet
        Class mentionType = args.mentionType
        Closure inferConcepts = args.inferConcepts

        java.util.List<IdentifiedAnnotation> mentions = []
        searchSet.each { Annotation ann ->
            patterns.each { Pattern pattern, Map vals ->
                Matcher matcher = ann.coveredText =~ pattern
                matcher.each {
                    // create an annotation for each match
                    IdentifiedAnnotation mention = create(
                            type:mentionType,
                            begin:(matcher.start(vals.group) + ann.begin),
                            end:(matcher.end(vals.group) + ann.begin)
                            )
                    OntologyConcept concept = create(vals)
                    if (concept.codingScheme == 'UCUM') {
                        concept.code = matcher.group('quant') + matcher.group('unit')
                    }
                    if (concept.oid == null) {
                        concept.oid = "${vals.code}#${vals.codingScheme}"
                    }
                    java.util.List<OntologyConcept> concepts = [concept]
                    if (inferConcepts) {
                        concepts = concepts + inferConcepts.call(concept)
                    }
                    mention.ontologyConcepts = concepts
                    mentions.add(mention)
                }
            }
        }
        
		mentions
    }
    
    // ------------------------------------------------------------------------
    // Annotation selection functions
    // ------------------------------------------------------------------------

    static select(Map args) {
        Class type = args.type
        Closure filter = args.filter

        Collection<AnnotationFS> annotations = (type != null ? select(jcas, type) : selectAll(jcas))

        if (filter) {
            Collection<AnnotationFS> filtered = []
            annotations.each {
                if (filter.call(it) == true) { filtered << it }
            }
            annotations = filtered
        }

        annotations
    }


    // ------------------------------------------------------------------------
    // Selection predicates
    // TODO: check UIMAFit and RUTA for more complete set
    // ------------------------------------------------------------------------

	static not = { Closure pred ->
		{ TOP ann ->
			!pred.call(ann)
		}
	}

    static and = { Closure... preds ->
        { TOP ann ->
            for (Closure pred : preds) {
                if (pred.call(ann) == false) { return false }
            }
            true
        }
    }

    static or = { Closure... preds ->
        { TOP ann ->
            for (Closure pred : preds) {
                if (pred.call(ann) == true) { return true }
            }
            false
        }
    }

    static contains = { Class<? extends Annotation> type ->
        { TOP ann ->
            contains(jcas, ann, type)
        }
    }

    static coveredBy = { TOP coveringAnn ->
        { TOP ann ->
            (ann != coveringAnn &&
            coveringAnn.begin <= ann.begin &&
            coveringAnn.end >= ann.end)
        }
    }

    static between = { Integer begin, Integer end ->
        { TOP ann ->
            (begin <= end && begin <= ann.begin && end >= ann.end)
        }
    }

    static before = { Integer index ->
        { TOP ann ->
            ann.end < index
        }
    }

    static after = { Integer index ->
        { TOP ann ->
            ann.begin > index
        }
    }
}