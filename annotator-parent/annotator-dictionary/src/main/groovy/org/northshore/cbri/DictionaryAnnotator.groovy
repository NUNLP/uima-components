package org.northshore.cbri

import static org.apache.uima.fit.util.CasUtil.getType
import static org.apache.uima.fit.util.JCasUtil.select
import static org.apache.uima.fit.util.JCasUtil.selectCovered
import groovy.util.logging.Log4j

import java.lang.reflect.Type

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

import de.tudarmstadt.ukp.dkpro.core.api.parameter.ComponentParameters
import de.tudarmstadt.ukp.dkpro.core.api.resources.ResourceUtils

@Log4j
public class DictionaryAnnotator
extends JCasAnnotator_ImplBase {
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


    private PhraseTree phrases
    private Map<List<String>, Map<String, String>> phraseSems
    private Type collectionType


    @Override
    public void initialize(UimaContext aContext)
    throws ResourceInitializationException {
        super.initialize(aContext)

        this.phrases = new PhraseTree()
        this.phraseSems = new HashMap<>()
        this.collectionType = new TypeToken<HashMap<String, String>>(){}.getType()
        
        GsonBuilder builder = new GsonBuilder()
        Gson gson = builder.create()
        URL dictUrl = ResourceUtils.resolveLocation(phraseFile, aContext)
        new File(dictUrl.toURI()).withReader(this.modelEncoding) { reader ->
            // each line is a dictionary entry in json format
            String line = null
            while ((line = reader.readLine()) != null) {
                Map<String, String> dictEntryMap = gson.fromJson(line, collectionType)
                String[] phraseSplit = dictEntryMap['phrase'].toLowerCase().split(/ /)
                phrases.addPhrase(phraseSplit)
                // add phrase semantics
                logger.info "phrase: ${dictEntryMap['phrase']}"
                dictEntryMap.remove("phrase")
                this.phraseSems.put(Arrays.asList(phraseSplit), dictEntryMap)
            }
        }
    }

    @Override
    public void process(JCas jcas)
            throws AnalysisEngineProcessException
    {
        UIMAUtil.jcas = jcas

        for (Sentence currSentence : select(jcas, Sentence)) {
            ArrayList<BaseToken> tokens = new ArrayList<BaseToken>(selectCovered(BaseToken, currSentence))

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

                    Map vals = this.phraseSems.get(Arrays.asList(longestMatch))

                    Class typeClass = UIMAUtil.getIdentifiedAnnotationClass(vals['type'])

                    UIMAUtil.create(type:typeClass, begin:beginToken.getBegin() ,
                    end:endToken.getEnd(), polarity:1 , uncertainty:0,
                    ontologyConcepts:[
                        UIMAUtil.create(type:UmlsConcept, code:vals["code"],
                        codingScheme:vals["codingScheme"],
                        cui:vals["cui"], tui:vals["tui"])]
                    )
                }
            }
        }
    }
}
