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
package org.northshore.cbri;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import opennlp.tools.sentdetect.DefaultSDContextGenerator;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.uima.sentdetect.SentenceModelResource;

import org.apache.ctakes.typesystem.type.textspan.Segment;
import org.apache.ctakes.typesystem.type.textspan.Sentence;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.annotator.AnnotatorProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.resource.ResourceInitializationException;

import com.google.common.io.Resources;

/**
 * Wraps the OpenNLP sentence detector in a UIMA annotator
 * 
 * @author Mayo Clinic
 */
public class SentenceDetector extends JCasAnnotator_ImplBase {

    public static final String SD_MODEL_FILE_PARAM = "sentenceModelFile";

    @ConfigurationParameter(name = SD_MODEL_FILE_PARAM, mandatory = true, description = "File holding sentence model")
    private String sentenceModelFile;

    private opennlp.tools.sentdetect.SentenceModel sdmodel;
    private SentenceDetectorCtakes sentenceDetector;

    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {

        super.initialize(aContext);

        try {
            InputStream modelIn = Resources.getResource(sentenceModelFile).openStream();
            this.sdmodel = new SentenceModel(modelIn);
            EndOfSentenceScannerImpl eoss = new EndOfSentenceScannerImpl();
            char[] eosc = eoss.getEndOfSentenceCharacters();
            DefaultSDContextGenerator cg = new DefaultSDContextGenerator(eosc);
            sentenceDetector = new SentenceDetectorCtakes(sdmodel.getMaxentModel(), cg, eoss);
        } catch (Exception ace) {
            throw new ResourceInitializationException(ace);
        }
    }

    /**
     * Entry point for processing.
     */
    public void process(JCas jcas) throws AnalysisEngineProcessException {

        int sentenceCount = 0;

        String text = jcas.getDocumentText();

        ////(new Segment(jcas, 0, text.length())).addToIndexes();
        JFSIndexRepository indexes = jcas.getJFSIndexRepository();
        Iterator<?> sectionItr = indexes.getAnnotationIndex(Segment.type).iterator();
        while (sectionItr.hasNext()) {
            Segment sa = (Segment) sectionItr.next();
            sentenceCount = annotateRange(jcas, text, sa, sentenceCount);
        }
    }

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
    protected int annotateRange(JCas jcas, String text, Segment section, int sentenceCount)
            throws AnalysisEngineProcessException {

        int b = section.getBegin();
        int e = section.getEnd();

        // Use OpenNLP tools to split text into sentences
        // The sentence detector returns the offsets of the sentence-endings it
        // detects within the string
        int[] sentenceBreaks = sentenceDetector.sentPosDetect(text.substring(b, e));

        int numSentences = sentenceBreaks.length;
        // There might be text after the last sentence-ending found by detector,
        // so +1
        SentenceSpan[] potentialSentSpans = new SentenceSpan[numSentences + 1];

        int sentStart = b;
        int sentEnd = b;
        // Start by filling in sentence spans from what OpenNLP tools detected
        // Will trim leading or trailing whitespace when check for end-of-line
        // characters
        for (int i = 0; i < numSentences; i++) {
            sentEnd = sentenceBreaks[i] + b;
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
        if (sentEnd < e) {
            String coveredText = text.substring(sentEnd, e);
            if (coveredText.trim() != "") {
                potentialSentSpans[numSentences] = new SentenceSpan(sentEnd, e, coveredText);
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
}
