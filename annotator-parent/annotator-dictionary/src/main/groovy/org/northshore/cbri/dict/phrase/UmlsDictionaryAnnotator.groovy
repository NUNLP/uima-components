package org.northshore.cbri.dict.phrase

import static org.apache.uima.fit.util.CasUtil.getType
import static org.apache.uima.fit.util.JCasUtil.select
import static org.apache.uima.fit.util.JCasUtil.selectCovered
import groovy.json.JsonSlurper
import groovy.util.logging.Log4j
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.tools.util.Span
import opennlp.uima.tokenize.TokenizerModelResource

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.fit.descriptor.ExternalResource
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceAccessException
import org.apache.uima.resource.ResourceInitializationException
import org.northshore.cbri.dsl.UIMAUtil

@Log4j
public class UmlsDictionaryAnnotator extends JCasAnnotator_ImplBase {
    
    @ExternalResource(key = 'token_model')
    TokenizerModelResource modelResource

    public static final String PARAM_DICTIONARY_FILE = 'dictFile'
    @ConfigurationParameter(name = 'dictFile', mandatory = true)
    private String dictFile
    
    public static final String PARAM_DICTIONARY_FILE_ENCODING = 'dictFileEncoding'
    @ConfigurationParameter(name = 'dictFileEncoding', mandatory = true, defaultValue='UTF-8')
    private String dictFileEncoding
    
    private PhraseTree phrases
    private Map<List<String>, Map<String, String>> phraseSems

    @Override
    public void initialize(UimaContext aContext)
    throws ResourceInitializationException {
        super.initialize(aContext)

        this.phrases = new PhraseTree()
        this.phraseSems = new HashMap<>()
        
        TokenizerModel model
        try {
            model = modelResource.getModel()
        } catch (ResourceAccessException e) {
            throw new ResourceInitializationException(e)
        }
        TokenizerME tokenizer = new TokenizerME(model)
        
        JsonSlurper slurper = new JsonSlurper()
        File dictContents = new File(this.getClass().getResource(this.dictFile).getFile())
        dictContents.eachLine { String line ->
            // add phrase
            Map<String, String> dictEntryMap = slurper.parseText(line)
            
            String phrase = dictEntryMap['phrase']
            List<String> phraseSplit = []
            Span[] tokenSpans = tokenizer.tokenizePos(phrase)
            tokenSpans.each { Span span ->
                phraseSplit << phrase.substring(span.getStart(), span.getEnd()).toLowerCase()
            }
            phrases.addPhrase(phraseSplit as String[])
            
            // add phrase semantics
            logger.info "phrase: ${phrase}"
            dictEntryMap.remove('phrase')
            this.phraseSems.put(phraseSplit, dictEntryMap)
        }
    }

    @Override
    public void process(JCas jcas)
            throws AnalysisEngineProcessException
    {
        UIMAUtil.jcas = jcas

        for (Sentence currSentence : select(jcas, Sentence)) {
            List<BaseToken> tokens = new ArrayList<BaseToken>(selectCovered(BaseToken, currSentence))
            for (int i = 0; i < tokens.size(); i++) {
                List<BaseToken> tokensToSentenceEnd = tokens.subList(i, tokens.size() - 1)
                String[] sentenceToEnd = new String[tokens.size()]

                for (int j = 0; j < tokensToSentenceEnd.size(); j++) {
                    sentenceToEnd[j] = tokensToSentenceEnd.get(j).coveredText.toLowerCase()
                }

                String[] longestMatch = phrases.getLongestMatch(sentenceToEnd)
                if (longestMatch != null) {
                    BaseToken beginToken = tokens.get(i)
                    BaseToken endToken = tokens.get(i + longestMatch.length - 1)

                    Map phraseSem = this.phraseSems.get(Arrays.asList(longestMatch))
                    
                    Class typeClass = UIMAUtil.getIdentifiedAnnotationClass(phraseSem['type'])
                    UIMAUtil.create(type:typeClass, begin:beginToken.getBegin() ,
                    end:endToken.getEnd(), polarity:1 , uncertainty:0, 
					sentenceID:currSentence.sentenceNumber,
                    ontologyConcepts:[
                        UIMAUtil.create(type:UmlsConcept, code:phraseSem['code'],
                        codingScheme:phraseSem['codingScheme'],
                        cui:phraseSem['cui'], tui:phraseSem['tui'])]
                    )
                }
            }
        }
    }
}
