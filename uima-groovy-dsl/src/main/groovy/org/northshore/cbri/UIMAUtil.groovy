package org.northshore.cbri

import static org.apache.uima.fit.util.JCasUtil.*

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.ctakes.typesystem.type.refsem.OntologyConcept
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation
import org.apache.uima.cas.text.AnnotationFS
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.cas.FSArray
import org.apache.uima.jcas.cas.TOP
import org.apache.uima.jcas.tcas.Annotation

/**
 * UIMAUtil implements the UIMA DSL
 * @author Will Thompson
 */
class UIMAUtil extends Script {
    @Override
    public Object run() {
        return super.run()
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
        IdentifiedAnnotation.metaClass.setOntologyConcepts = { List concepts ->
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
            Matcher.metaClass.namedGroupIndex = new HashMap<String, Integer>()
            int groupCnt = 0
            m.each {
                groupCnt += 1
                if (m.group(3) != null) {
                    println "Meta-match: (" + m.group(3) + ") from: " + m.start(3) + " to " + m.end(3)
                    retval.namedGroupIndex[m.group(3)] = groupCnt
                }
            }
            return retval
        }
        
        Pattern.metaClass.matcher = { Map args ->
            Annotation coveringAnn = (args.coveringAnn ?: jcas.documentAnnotationFs)
            Boolean includeText = (args.includeText == false ? false : true)
            List<Class<Annotation>> types = (args.types ?: [])
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

    // ------------------------------------------------------------------------
    // Annotation creation functions
    // ------------------------------------------------------------------------

    /**
     * Create UIMA annotation from set of attributes
     * @param attrs
     * @return
     */
    static TOP create(Map attrs) {
        TOP a = attrs.type.newInstance(jcas);
        attrs.each { k, v ->
            if (a.metaClass.hasProperty(a, k)) {
                if (k != "type") {
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
    def static applyPatterns = { Collection<Annotation> anns, List<Pattern> patterns, Closure action ->
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
    def static List<IdentifiedAnnotation> createMentions(Map args) {
        Map patterns = args.patterns
        Collection searchSet = args.searchSet
        Class mentionType = args.mentionType
        Closure inferConcepts = args.inferConcepts

        List<IdentifiedAnnotation> mentions = []
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
                    if (concept.codingScheme == "UCUM") {
                        concept.code = matcher.group("quant") + matcher.group("unit")
                    }
                    if (concept.oid == null) {
                        concept.oid = "${vals.code}#${vals.codingScheme}"
                    }
                    List<OntologyConcept> concepts = [concept]
                    if (inferConcepts) {
                        concepts = concepts + inferConcepts.call(concept)
                    }
                    mention.ontologyConcepts = concepts
                    mentions.add(mention)
                }
            }
        }
        return mentions
    }
    
    // ------------------------------------------------------------------------
    // Annotation selection functions
    // ------------------------------------------------------------------------

    def static select(Map args) {
        Class type = args.type
        Closure filter = args.filter

        def Collection<AnnotationFS> annotations = (type != null ? select(jcas, type) : selectAll(jcas))

        if (filter) {
            def Collection<AnnotationFS> filtered = []
            annotations.each {
                if (filter.call(it) == true) { filtered << it }
            }
            annotations = filtered
        }

        return annotations
    }


    // ------------------------------------------------------------------------
    // Selection predicates
    // TODO: check UIMAFit and RUTA for more complete set
    // ------------------------------------------------------------------------

        def static not = { Closure pred ->
        Closure c = { AnnotationFS ann ->
            return !pred.call(ann)
        }
    }

    def static and = { Closure... preds ->
        Closure c = { AnnotationFS ann ->
            for (Closure pred : preds) {
                if (pred.call(ann) == false) { return false }
            }
            return true
        }
    }

    def static or = { Closure... preds ->
        Closure c = { AnnotationFS ann ->
            for (Closure pred : preds) {
                if (pred.call(ann) == true) { return true }
            }
            return false
        }
    }

    def static contains = { Class<? extends Annotation> type ->
        Closure c = { AnnotationFS ann ->
            return contains(jcas, ann, type)
        }
    }

    def static coveredBy = { AnnotationFS coveringAnn ->
        Closure c = { AnnotationFS ann ->
            return ((ann != coveringAnn &&
            coveringAnn.begin <= ann.begin &&
            coveringAnn.end >= ann.end) ? true : false)
        }
    }

    def static between = { Integer begin, Integer end ->
        Closure c = { AnnotationFS ann ->
            return ((begin <= end && begin <= ann.begin && end >= ann.end) ? true : false)
        }
    }

    def static before = { Integer index ->
        Closure c = { AnnotationFS ann ->
            return ((ann.end < index) ? true : false)
        }
    }

    def static after = { Integer index ->
        Closure c = { AnnotationFS ann ->
            return ((ann.begin > index) ? true : false)
        }
    }
}