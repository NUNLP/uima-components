package org.northshore.cbri.dict.lingpipe

import static org.apache.uima.fit.util.CasUtil.getType
import static org.apache.uima.fit.util.JCasUtil.select
import static org.apache.uima.fit.util.JCasUtil.selectCovered
import groovy.json.JsonSlurper
import groovy.util.logging.Log4j
import opennlp.tools.tokenize.TokenizerModel
import opennlp.uima.tokenize.TokenizerModelResource

import org.apache.ctakes.typesystem.type.refsem.UmlsConcept
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.fit.descriptor.ExternalResource
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceAccessException
import org.apache.uima.resource.ResourceInitializationException
import org.northshore.cbri.dsl.UIMAUtil

import com.aliasi.chunk.Chunk
import com.aliasi.chunk.Chunking
import com.aliasi.spell.FixedWeightEditDistance
import com.aliasi.spell.WeightedEditDistance

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
    
    public static final String PARAM_MAX_DISTANCE = 'maxDistance'
    @ConfigurationParameter(name = 'maxDistance', mandatory = true)
    private Integer maxDistance

    // the chunker instance
    private ApproxDictionaryChunker chunker;


    @Override
    public void initialize(UimaContext aContext)
    throws ResourceInitializationException {
        super.initialize(aContext)

        // make the dictionary and load contents
        TrieDictionary<String> dict = new TrieDictionary<String>()

        JsonSlurper slurper = new JsonSlurper()
        File dictContents = new File(this.getClass().getResource(this.dictFile).getFile())
        dictContents.eachLine { String line ->
            Map<String, String> dictEntryMap = slurper.parseText(line)
            String phrase = dictEntryMap['phrase']
            String cui = dictEntryMap['cui']
            println "Phrase: ${phrase}, CUI: ${cui}"
            dict.addEntry(new DictionaryEntry<String>(phrase, cui))
        }
        
        // extract the tokenizer model resource
        TokenizerModel model
        try {
            model = modelResource.getModel()
        } catch (ResourceAccessException e) {
            throw new ResourceInitializationException(e)
        }

        // make the chunker instance
        WeightedEditDistance editDistance = new FixedWeightEditDistance(0,-1,-1,-1,Double.NaN)
        chunker = new ApproxDictionaryChunker(dict,
                new OpenNLPTokenizerFactory(model),
                editDistance,
                maxDistance)

    }

    @Override
    public void process(JCas jcas)
            throws AnalysisEngineProcessException
    {
        UIMAUtil.jcas = jcas
        
        Chunking chunking = chunker.chunk(jcas.documentText)
        ////CharSequence cs = chunking.charSequence()
        Set<Chunk> chunkSet = chunking.chunkSet()
        chunkSet.each { Chunk chunk ->
            int start = chunk.start()
            int end = chunk.end()
            ////CharSequence str = cs.subSequence(start,end)
            double distance = chunk.score()
            UIMAUtil.create(type:IdentifiedAnnotation, begin:start, end:end,
                ontologyConcepts:[UIMAUtil.create(type:UmlsConcept, cui:chunk.type())])
        }
    }
}
