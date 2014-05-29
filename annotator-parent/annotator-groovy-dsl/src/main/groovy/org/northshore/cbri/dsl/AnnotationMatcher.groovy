package org.northshore.cbri.dsl

import static org.apache.uima.fit.util.JCasUtil.*

import java.util.regex.MatchResult
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.uima.cas.text.AnnotationFS
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.codehaus.groovy.runtime.StringGroovyMethods

// TODO: remove types argument, infer from short name and types of entities covered
// TODO: incorporate named group methods into AnnotationMatcher (?)
// TODO: handle super-types ("Annotation" should match all annotations)
// TODO: generate multiple match strings as needed
// TODO: optimize collections and types

/**
 * AnnotationMatcher class
 * @author Will Thompson
 *
 */
class AnnotationMatcher implements MatchResult {
        
    class AnnotationMatcherIterator implements Iterator {
        Iterator matcherIter
        AnnotationMatcherIterator() {
            matcherIter = StringGroovyMethods.iterator(AnnotationMatcher.this.matcher)
        }

        public boolean hasNext() {
            return this.matcherIter.hasNext()
        }

        public Object next() {
            Object result = this.matcherIter.next()
            AnnotationMatcher outer = AnnotationMatcher.this
            Map matchBinding = [:]
            outer.matcher.namedGroupIndex.each { k, v ->
                int groupNum = outer.matcher.start(k)
                if (groupNum != null) {
                    matchBinding[k] = outer.matchStringBinding.get(groupNum)
                }
            }
            return matchBinding
        }

        public void remove() {
            this.matcherIter.remove()
        }
    }
    
    // ------------------------------------------------------------------------
    // Fields
    // ------------------------------------------------------------------------
    
    public final Annotation ann;
    public final Matcher matcher;
    Map<String, Integer> namedGroupIndex;
    Map<Integer, List<Annotation>> annBeginPosIndex;
    Map<Integer, AnnotationFS> matchStringBinding;
    
    AnnotationMatcher(JCas jcas, Annotation ann, List<Class<Annotation>> annTypes, 
        Pattern pattern, Boolean includeText) {
        
        this.ann = ann
        this.namedGroupIndex = new HashMap<String, Integer>()
        this.matchStringBinding = new HashMap<Integer, AnnotationFS>()
        this.annBeginPosIndex = new HashMap<Integer, List<Annotation>>()
        
//        selectCovered(jcas, Annotation, ann.begin, ann.end).each {
//            println "covered type: " + it.class.simpleName
//        }
        
        // create an index of annotations based on starting position
        annTypes.each {
            selectCovered(jcas, it, ann.begin, ann.end).each {
                List<Annotation> anns = this.annBeginPosIndex.get(it.begin)
                if (anns != null) {
                    anns.add(it)
                }
                else {
                    this.annBeginPosIndex.put(it.begin, [it] as List)
                }
            }
        }
        
        this.matcher = pattern.matcher(this.createMatchString(ann, includeText))
    }
    
    // TODO: create a set of matchers, one for each match string
    private String createMatchString(Annotation coveringAnn, Boolean includeText) {
        String text = coveringAnn.coveredText
        String matchString = ""
        Integer i = 0
        while (i < text.length()) {
            List<Annotation> annList = this.annBeginPosIndex.get(i+coveringAnn.begin)
            if (annList != null && annList.size() > 0) {
                Annotation ann = annList[0]
                this.matchStringBinding[matchString.length()] = ann
                matchString += ('@' + ann.class.simpleName)
                if (ann.coveredText.length() == 0) {
                    if (includeText) { matchString += text[i] }
                    i += 1
                }
                else {
                    i += ann.coveredText.length()
                }
            }
            else {
                if (includeText) { matchString += text[i] }
                i += 1
            }
        }
        // handle case of zero length annotation at end of text
        List<Annotation> annList = this.annBeginPosIndex.get(coveringAnn.end)
        if (annList != null && annList.size() > 0) {
            Annotation ann = annList[0]
            this.matchStringBinding[matchString.length()] = ann
            matchString += ('@' + ann.class.simpleName)
        }
        
        ////println "Annotation match string: " + matchString
        return matchString
    }
    
    public int start() {
        return (this.matcher.start() + this.ann.begin)
    }

    public int start(int group) {
        return (this.matcher.start(group) + this.ann.begin)
    }

    public int start(String name) {
        return (this.matcher.start(name) + this.ann.begin)
    }

    public int end() {
        return (this.matcher.end() + this.ann.begin)
    }

    public int end(int group) {
        return (this.matcher.end(group) + this.ann.begin)
    }
    
    public int end(String name) {
        return (this.matcher.end(name) + this.ann.begin)
    }

    public String group() {
        return this.matcher.group()
    }
    
    public String group(int group) {
        return this.matcher.group(group)
    } 

    public int groupCount() {
        return this.matcher.groupCount()
    }
    
    public Iterator iterator() {
        return new AnnotationMatcherIterator()
    }
}
