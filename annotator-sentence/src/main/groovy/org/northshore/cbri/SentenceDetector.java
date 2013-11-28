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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

import opennlp.tools.sentdetect.DefaultSDContextGenerator;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

import org.apache.ctakes.typesystem.type.textspan.Segment;
import org.apache.ctakes.typesystem.type.textspan.Sentence;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.annotator.AnnotatorProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.resource.ResourceAccessException;
import org.apache.uima.resource.ResourceInitializationException;

import com.google.common.io.Resources;

/**
 * Wraps the OpenNLP sentence detector in a UIMA annotator
 * 
 * @author Mayo Clinic
 */
public class SentenceDetector extends JCasAnnotator_ImplBase {
	/**
	 * Value is "SegmentsToSkip". This parameter specifies which sections to
	 * skip. The parameter should be of type String, should be multi-valued and
	 * optional.
	 */
	public static final String PARAM_SEGMENTS_TO_SKIP = "SegmentsToSkip";

	// LOG4J logger based on class name
	private Logger logger = Logger.getLogger(getClass().getName());

	public static final String SD_MODEL_FILE_PARAM = "SentenceModelFile";

	private opennlp.tools.sentdetect.SentenceModel sdmodel;

	private UimaContext context;

	private Set<?> skipSegmentsSet;

	private SentenceDetectorCtakes sentenceDetector;

	private String NEWLINE = "\n";

	private int sentenceCount = 0;

	public void initialize(UimaContext aContext)
			throws ResourceInitializationException {

		super.initialize(aContext);

		context = aContext;
		try {
			configInit();
		} catch (Exception ace) {
			throw new ResourceInitializationException(ace);
		}
	}

	/**
	 * Reads configuration parameters.
	 * 
	 * @throws ResourceAccessException
	 * @throws IOException 
	 * @throws InvalidFormatException 
	 */
	private void configInit() throws ResourceAccessException, InvalidFormatException, IOException {

		String sdModelPath = (String) context
				.getConfigParameterValue(SD_MODEL_FILE_PARAM);
//			InputStream is = FileLocator.getAsStream(sdModelPath);
		    InputStream is = Resources.getResource(sdModelPath).openStream();
			logger.info("Sentence detector model file: " + sdModelPath);
			sdmodel = new SentenceModel(is);
			is.close();
			EndOfSentenceScannerImpl eoss = new EndOfSentenceScannerImpl();
			char[] eosc = eoss.getEndOfSentenceCharacters();
			// SentenceDContextGenerator cg = new SentenceDContextGenerator();
			DefaultSDContextGenerator cg = new DefaultSDContextGenerator(eosc);
			sentenceDetector = new SentenceDetectorCtakes(
					sdmodel.getMaxentModel(), cg, eoss);

//			skipSegmentsSet = ParamUtil.getStringParameterValuesSet(
//					PARAM_SEGMENTS_TO_SKIP, context);
	}

	/**
	 * Entry point for processing.
	 */
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		logger.info("Starting processing.");

		sentenceCount = 0;

		String text = jcas.getDocumentText();

//		Segment segment = new Segment(jcas, 0, text.length());
//		segment.addToIndexes();
		JFSIndexRepository indexes = jcas.getJFSIndexRepository();
		Iterator<?> sectionItr = indexes.getAnnotationIndex(Segment.type)
				.iterator();
		while (sectionItr.hasNext()) {
			Segment sa = (Segment) sectionItr.next();
			String sectionID = sa.getId();
			if (true) {
				sentenceCount = annotateRange(jcas, text, sa, sentenceCount);
			}
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
	protected int annotateRange(JCas jcas, String text, Segment section,
			int sentenceCount) throws AnalysisEngineProcessException {

		int b = section.getBegin();
		int e = section.getEnd();

		// Use OpenNLP tools to split text into sentences
		// The sentence detector returns the offsets of the sentence-endings it
		// detects
		// within the string
		int[] sentenceBreaks = sentenceDetector.sentPosDetect(text.substring(b,
				e)); // OpenNLP tools 1.5 returns Spans rather than offsets that
						// 1.4 did
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
			sentEnd = sentenceBreaks[i] + b; // OpenNLP tools 1.5 returns Spans
												// rather than offsets that 1.4
												// did
			String coveredText = text.substring(sentStart, sentEnd);
			potentialSentSpans[i] = new SentenceSpan(sentStart, sentEnd,
					coveredText);
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
				potentialSentSpans[numSentences] = new SentenceSpan(sentEnd, e,
						coveredText);
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
				sentenceSpans.addAll(potentialSentSpans[i]
						.splitAtLineBreaksAndTrim(NEWLINE)); // TODO Determine
																// line break
																// type
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
					// System.out.println("Adding Sentence Annotation for " +
					// span.toString());
					sa.setSentenceNumber(sentenceCount);
					sa.addToIndexes();
					sentenceCount++;
					previousEnd = span.getEnd();
				}
			}
		}
		return sentenceCount;
	}

	/**
	 * Train a new sentence detector from the training data in the first file
	 * and write the model to the second file.<br>
	 * The training data file is expected to have one sentence per line.
	 * 
	 * @param args
	 *            training_data_filename name_of_model_to_create iters? cutoff?
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		final Logger logger = Logger.getLogger(SentenceDetector.class.getName()
				+ ".main()");

		// Handle arguments
		if (args.length < 2 || args.length > 4) {
			usage(logger);
			System.exit(-1);
		}

		File inFile = getReadableFile(args[0]);

		File outFile = getFileInExistingDir(args[1]);
		// File outFile = new File(args[1]);

		int iters = 100;
		if (args.length > 2) {
			iters = parseInt(args[2], logger);
		}

		int cut = 5;
		if (args.length > 3) {
			cut = parseInt(args[3], logger);
		}

		// Now, do the actual training
		EndOfSentenceScannerImpl scanner = new EndOfSentenceScannerImpl();
		int numEosc = scanner.getEndOfSentenceCharacters().length;

		logger.info("Training new model from " + inFile.getAbsolutePath());
		logger.info("Using " + numEosc + " end of sentence characters.");

	}

	public static void usage(Logger log) {
		log.info("Usage: java "
				+ SentenceDetector.class.getName()
				+ " training_data_filename name_of_model_to_create <iters> <cut>");
	}

	public static int parseInt(String s, Logger log) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			throw (nfe);
		}
	}

	public static File getReadableFile(String fn) throws IOException {
		File f = new File(fn);
		if (!f.canRead()) {
			throw new IOException("Unable to read from file "
					+ f.getAbsolutePath());
		}
		return f;
	}

	public static File getFileInExistingDir(String fn) throws IOException {
		File f = new File(fn);
		if (!f.getParentFile().isDirectory()) {
			throw new IOException("Directory not found: "
					+ f.getParentFile().getAbsolutePath());
		}
		return f;
	}

}
