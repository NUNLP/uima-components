/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.northshore.cbri.sent;

import groovy.lang.GroovyShell;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import opennlp.model.AbstractModel;
import opennlp.tools.sentdetect.DefaultSDContextGenerator;
import opennlp.tools.sentdetect.EndOfSentenceScanner;
import opennlp.tools.sentdetect.SDContextGenerator;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.StringUtil;

import org.apache.ctakes.typesystem.type.textspan.Segment;
import org.apache.ctakes.typesystem.type.textspan.Sentence;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.annotator.AnnotatorProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;


public class SentenceDetector_cTAKES extends JCasAnnotator_ImplBase {
    
    private static class SentenceSpan {

        public static String LF = "\n";
        public static String CR = "\r";
        public static String CRLF = "\r\n";

        private int start; // offset of text within larger text
        private int end;   // offset of end of text within larger text
        private String text;
        
        public SentenceSpan(int s, int e, String t){
            start = s;
            end = e;
            text = t;
        }

        public int getStart() {return start;}
        public int getEnd() {return end;}
        
        /**
         * Trim any leading or trailing whitespace.
         * If there are any end-of-line characters in what's left, split into multiple smaller sentences,
         * and trim each.
         * If is entirely whitespace, return an empty list
         * @param separatorPattern CR LF or CRLF
         */
        public List<SentenceSpan> splitAtLineBreaksAndTrim(String separatorPattern) {
            
            ArrayList<SentenceSpan> subspans = new ArrayList<SentenceSpan>();

            // Validate input parameter
            if (!separatorPattern.equals(LF) && !separatorPattern.equals(CR) && !separatorPattern.equals(CRLF)) {
                
                int len = separatorPattern.length();
                System.err.println("Invalid line break: " + len + " characters long.");
                
                System.err.print("        line break character values: ");
                for (int i=0; i<len; i++){
                    System.err.print(Integer.valueOf(separatorPattern.charAt(i)));
                    System.err.print(" "); // print a space between values
                }
                System.err.println();
            
                //System.err.println("Invalid line break: \\0x" + Byte.parseByte(separatorPattern.getBytes("US-ASCII").toString(),16));
                subspans.add(this);
                return subspans;
            }
            
            // Check first if contains only whitespace, in which case return an empty list
            String coveredText = text.substring(0, end-start);
            String trimmedText = coveredText.trim();
            int trimmedLen = trimmedText.length();
            if (trimmedLen == 0) {
                return subspans;
            }
            
            // If there is any leading or trailing whitespace, determine position of the trimmed section
            int positionOfNonWhiteSpace = 0;
            
            // Split into multiple sentences if contains end-of-line characters
            // or return just one sentence if no end-of-line characters are within the trimmed string
            String spans[] = coveredText.split(separatorPattern);
            int position = start;
            for (String s : spans) {
                String t = s.trim();
                if (t.length()>0) {
                    positionOfNonWhiteSpace = s.indexOf(t.charAt(0));
                } else {
                    positionOfNonWhiteSpace = 0;
                }
                // Might have trimmed off some at the beginning of the sentences other than the 1st (#0)
                position += positionOfNonWhiteSpace; // sf Bugs artifact 3083903: For _each_ sentence, advance past any spaces at beginning of line
                subspans.add(new SentenceSpan(position, position+t.length(), t));
                position += (s.length()-positionOfNonWhiteSpace + separatorPattern.length());
            }
            
            return subspans;

        }
    }
    
    /**
     * 
     * @author wthompso
     *
     */
    private static class EndOfSentenceScannerImpl implements EndOfSentenceScanner {

        private static final char[] eosCandidates =  {'.', '!', ')', ']', '>', '\"', ':', ';'}; // CTAKES-227

        public EndOfSentenceScannerImpl() {
            super();
        }

        public char[] getEndOfSentenceCharacters() {
            return eosCandidates;
        }

        public List<Integer> getPositions(String s) {
            return getPositions(s.toCharArray());
        }

        public List<Integer> getPositions(StringBuffer sb) {
            return getPositions(sb.toString().toCharArray());
        }
        
        public List<Integer> getPositions(char[] cb) {
            List<Integer> positions = new ArrayList<Integer>();

            for (int i=0; i<cb.length; i++) { // for each character in buffer
                for (int j=0; j<eosCandidates.length; j++) { // for each eosCandidate
                    if (cb[i]==eosCandidates[j]) { 
                        positions.add(new Integer(i)); // TODO - don't always create new, use a pool
                        break; // can't match others if it matched eosCandidates[j]
                    }
                }
            }
             
            return positions;
        }
    }

    //----------------------------------------------------------------------------------------------------------------
    // The main class definition starts here
    //----------------------------------------------------------------------------------------------------------------
    
    public static final String SD_MODEL_FILE_PARAM = "sentenceModelFile";
    public static final String SD_SEGMENTS_TO_PARSE = "segmentsToParse";

    @ConfigurationParameter(name = SD_MODEL_FILE_PARAM, mandatory = true, description = "File holding sentence model")
    private String sentenceModelFile;
    
    @ConfigurationParameter(name = SD_SEGMENTS_TO_PARSE, mandatory = false, description = "Script providing input segments")
    private String segmentsToParse;

    private opennlp.tools.sentdetect.SentenceModel sdmodel;
    private SDContextGenerator cgen;
    private EndOfSentenceScanner scanner;

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {

        super.initialize(aContext);

        try {
            InputStream modelIn = Resources.getResource(sentenceModelFile).openStream();
            this.sdmodel = new SentenceModel(modelIn);
            this.scanner = new EndOfSentenceScannerImpl();
            this.cgen = new DefaultSDContextGenerator(this.scanner.getEndOfSentenceCharacters());
        } catch (Exception ace) {
            throw new ResourceInitializationException(ace);
        }
    }
    
    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        
        //------------------------------------------------------------------------------------------------------------
        // TODO: this is experimental code on injecting a Groovy script to determine
        // which Segments should be run through the sentence detector annotator.
        Collection<Segment> segs = null;
        try {
            if (segmentsToParse != null) {
                CompilerConfiguration config = new CompilerConfiguration();
                config.setScriptBaseClass("org.northshore.cbri.dsl.UIMAUtil");
                ////Binding binding = new Binding();
                GroovyShell shell = new GroovyShell(config);
                
                System.out.println("GroovyAnnotator loading script file: " + this.segmentsToParse);
                URL url = Resources.getResource(this.segmentsToParse);
                String scriptContents = Resources.toString(url, Charsets.UTF_8);
                segs = (Collection<Segment>)shell.evaluate(scriptContents);
            }
            else {
                segs = JCasUtil.select(jcas, Segment.class);
            }
        } catch (CompilationFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
        //------------------------------------------------------------------------------------------------------------

        String text = jcas.getDocumentText();
        
         for (Segment seg : segs) {
            annotateRange(jcas, text, seg, 0);
        }
    }

    // ---------------------------------------------------------------------------------------------------------
    // Private methods
    // ---------------------------------------------------------------------------------------------------------

    /**
     * Detect sentences within a section of the text and add annotations to the
     * CAS. Uses OpenNLP sentence detector, and then additionally forces
     * sentences to end at end-of-line characters (splitting into multiple
     * sentences). Also trims sentences. And if the sentence detector does
     * happen to form a sentence that is just white space, it will be ignored.
     * 
     * @param jcas
     *            view of the CAS containing the text to run sentence detector
     *            against
     * @param text
     *            the document text
     * @param section
     *            the section this sentence is in
     * @param sentenceCount
     *            the number of sentences added already to the CAS (if
     *            processing one section at a time)
     * @return count The sum of <code>sentenceCount</code> and the number of
     *         Sentence annotations added to the CAS for this section
     * @throws AnnotatorProcessException
     */
    private int annotateRange(JCas jcas, String text, Segment section, int sentenceCount)
            throws AnalysisEngineProcessException {

        int sectBegin = section.getBegin();
        int sectEnd = section.getEnd();

        // Use OpenNLP tools to split text into sentences
        // The sentence detector returns the offsets of the sentence-endings it
        // detects within the string
        int[] sentenceBreaks = sentPosDetect(text.substring(sectBegin, sectEnd));

        int numSentences = sentenceBreaks.length;
        // There might be text after the last sentence-ending found by detector,
        // so +1
        SentenceSpan[] potentialSentSpans = new SentenceSpan[numSentences + 1];

        int sentStart = sectBegin;
        int sentEnd = sectBegin;
        
        // Start by filling in sentence spans from what OpenNLP tools detected
        // Will trim leading or trailing whitespace when check for end-of-line
        // characters
        for (int i = 0; i < numSentences; i++) {
            sentEnd = sentenceBreaks[i] + sectBegin;
            String coveredText = text.substring(sentStart, sentEnd);
            potentialSentSpans[i] = new SentenceSpan(sentStart, sentEnd, coveredText);
            sentStart = sentEnd;
        }

        // If detector didn't find any sentence-endings,
        // or there was text after the last sentence-ending found,
        // create a sentence from what's left, as long as it's not all
        // whitespace.
        // Will trim leading or trailing whitespace when check for end-of-line
        // characters
        if (sentEnd < sectEnd) {
            String coveredText = text.substring(sentEnd, sectEnd);
            if (coveredText.trim() != "") {
                potentialSentSpans[numSentences] = new SentenceSpan(sentEnd, sectEnd, coveredText);
                numSentences++;
            }
        }

        // Copy potentialSentSpans into sentenceSpans,
        // ignoring any that are entirely whitespace,
        // trimming the rest,
        // and splitting any of those that contain an end-of-line character.
        // Then trim any leading or trailing whitespace of ones that were split.
        ArrayList<SentenceSpan> sentenceSpans = new ArrayList<SentenceSpan>(0);
        for (int i = 0; i < potentialSentSpans.length; i++) {
            if (potentialSentSpans[i] != null) {
                sentenceSpans.addAll(potentialSentSpans[i].splitAtLineBreaksAndTrim("\n"));
            }
        }

        // Add sentence annotations to the CAS
        int previousEnd = -1;
        for (int i = 0; i < sentenceSpans.size(); i++) {
            SentenceSpan span = sentenceSpans.get(i);
            if (span.getStart() != span.getEnd()) { // skip empty lines
                Sentence sa = new Sentence(jcas);
                sa.setBegin(span.getStart());
                sa.setEnd(span.getEnd());
                if (previousEnd <= sa.getBegin()) {
                    sa.setSentenceNumber(sentenceCount);
                    sa.addToIndexes();
                    sentenceCount++;
                    previousEnd = span.getEnd();
                }
            }
        }
        return sentenceCount;
    }
        
    private int getFirstWS(String s, int pos) {
        while (pos < s.length() && !StringUtil.isWhitespace(s.charAt(pos)))
            pos++;
        return pos;
    }

    private int getFirstNonWS(String s, int pos) {
        while (pos < s.length() && StringUtil.isWhitespace(s.charAt(pos)))
            pos++;
        return pos;
    }

    /**
     * Detect the position of the first words of sentences in a String.
     * 
     * @param s
     *            The string to be processed.
     * @return A integer array containing the positions of the end index of
     *         every sentence
     * 
     * @see SentenceDetectorME#sentPosDetect(String)
     */
    private int[] sentPosDetect(String s) {
        StringBuffer sb = new StringBuffer(s);
        List<Integer> enders = this.scanner.getPositions(s);
        List<Integer> positions = new ArrayList<Integer>(enders.size());

        for (int i = 0, end = enders.size(), index = 0; i < end; i++) {
            Integer candidate = enders.get(i);
            int cint = candidate;
            // skip over the leading parts of non-token final delimiters
            int fws = getFirstWS(s, cint + 1);
            if (i + 1 < end && enders.get(i + 1) < fws) {
                continue;
            }

            AbstractModel model = this.sdmodel.getMaxentModel();
            double[] probs = model.eval(cgen.getContext(sb, cint));
            String bestOutcome = model.getBestOutcome(probs);
            if (bestOutcome.equals("s")) {
                if (index != cint) {
                    positions.add(getFirstNonWS(s, cint));
                }
                index = cint + 1;
            }
        }

        int[] sentenceBreaks = new int[positions.size()];
        for (int i = 0; i < sentenceBreaks.length; i++) {
            sentenceBreaks[i] = positions.get(i) + 1;
        }

        return sentenceBreaks;
    }
}
