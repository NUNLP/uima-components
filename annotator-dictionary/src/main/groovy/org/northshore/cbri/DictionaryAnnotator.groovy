package org.northshore.cbri

import static org.apache.uima.fit.util.CasUtil.getType
import static org.apache.uima.fit.util.JCasUtil.select
import static org.apache.uima.fit.util.JCasUtil.selectCovered

import org.apache.commons.io.IOUtils
import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textsem.EntityMention
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.cas.Type
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException

import de.tudarmstadt.ukp.dkpro.core.api.parameter.ComponentParameters
import de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils

/**
 * Takes a plain text file with phrases as input and annotates the phrases in the CAS file.
 *
 * The component requires that {@link Token}s and {@link Sentence}es are annotated in the CAS.
 *
 * The format of the phrase file is one phrase per line, tokens are separated by space:
 *
 * <pre>
 * this is a phrase
 * another phrase
 * </pre>
 */
public class DictionaryAnnotator
	extends JCasAnnotator_ImplBase
{
	/**
	 * The file must contain one phrase per line - phrases will be split at " "
	 */
	public static final String PARAM_MODEL_LOCATION = ComponentParameters.PARAM_MODEL_LOCATION
	@ConfigurationParameter(name = ComponentParameters.PARAM_MODEL_LOCATION, mandatory = true)
	private String phraseFile

	/**
	 * The character encoding used by the model.
	 */
	public static final String PARAM_MODEL_ENCODING = ComponentParameters.PARAM_MODEL_ENCODING
	@ConfigurationParameter(name = ComponentParameters.PARAM_MODEL_ENCODING, mandatory = true, defaultValue="UTF-8")
	private String modelEncoding

	/**
	 * The annotation to create on matching phases.
	 */
	public static final String PARAM_ANNOTATION_TYPE = "annotationType"
	@ConfigurationParameter(name = "annotationType", mandatory = false)
	private String annotationType

	private PhraseTree phrases
    private Map<List<String>, Map<String, String>> phraseSems

	@Override
	public void initialize(UimaContext aContext)
		throws ResourceInitializationException
	{
		super.initialize(aContext)

		if (annotationType == null) {
			annotationType = IdentifiedAnnotation.class.getName()
		}

		phrases = new PhraseTree()
        phraseSems = new HashMap<>()

        // TODO: this should read in from JSON or binary
		InputStream is = null
		try {
			URL phraseFileUrl = ResourceUtils.resolveLocation(phraseFile, aContext)
			is = phraseFileUrl.openStream()
			for (String inputLine : IOUtils.readLines(is, modelEncoding)) {
                String[] parts = inputLine.split(/\|/)
                assert parts.length > 0
                
                // add phrase
				String[] phraseSplit = parts[0].split(" ")
				phrases.addPhrase(phraseSplit)
                
                // add phrase semantics
                Map<String, String> sem = new HashMap<>()
                for (int n = 1; n < parts.length; n++) {
                    String[] featValPair = parts[n].split(/:/)
                    sem.put(featValPair[0], featValPair[1])
                }
                this.phraseSems.put(Arrays.asList(phraseSplit), sem)
			}
		}
		catch (IOException e) {
			throw new ResourceInitializationException(e)
		}
		finally {
			IOUtils.closeQuietly(is)
		}
	}

	@Override
	public void process(JCas jcas)
		throws AnalysisEngineProcessException
	{
		Type type = getType(jcas.getCas(), annotationType)

		for (Sentence currSentence : select(jcas, Sentence.class)) {
			ArrayList<BaseToken> tokens = new ArrayList<BaseToken>(selectCovered(BaseToken.class, currSentence))

			for (int i = 0; i < tokens.size(); i++) {
				List<BaseToken> tokensToSentenceEnd = tokens.subList(i, tokens.size() - 1)
				String[] sentenceToEnd = new String[tokens.size()]

				for (int j = 0; j < tokensToSentenceEnd.size(); j++) {
					sentenceToEnd[j] = tokensToSentenceEnd.get(j).getCoveredText()
				}

				String[] longestMatch = phrases.getLongestMatch(sentenceToEnd)

				if (longestMatch != null) {
					BaseToken beginToken = tokens.get(i)
					BaseToken endToken = tokens.get(i + longestMatch.length - 1)

//					AnnotationFS newFound = jcas.getCas().createAnnotation(type,
//							beginToken.getBegin(), endToken.getEnd())
//                    jcas.getCas().addFsToIndexes(newFound)
                    Map vals = this.phraseSems.get(Arrays.asList(longestMatch))
                    
                    UIMAUtil.jcas = jcas
                    UIMAUtil.create(type:EntityMention, begin:beginToken.getBegin() , 
                        end:endToken.getEnd(), polarity:1 , uncertainty :0,
                        ontologyConcepts:[UIMAUtil.create(type:UmlsConcept, code:vals["code"], 
                            codingScheme:vals["codingScheme"],
                            cui:vals["cui"], tui:vals["tui"])]
                        )
				}
			}
		}
	}
}
