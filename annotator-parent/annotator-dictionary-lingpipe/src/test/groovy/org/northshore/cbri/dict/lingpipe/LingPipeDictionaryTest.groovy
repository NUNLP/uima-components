package org.northshore.cbri.dict.lingpipe

import static org.junit.Assert.*

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.aliasi.chunk.Chunk
import com.aliasi.chunk.Chunking
import com.aliasi.dict.ApproxDictionaryChunker
import com.aliasi.dict.DictionaryEntry
import com.aliasi.dict.ExactDictionaryChunker
import com.aliasi.dict.MapDictionary
import com.aliasi.dict.TrieDictionary
import com.aliasi.spell.FixedWeightEditDistance
import com.aliasi.spell.WeightedEditDistance
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory

class LingPipeDictionaryTest {
    static final double CHUNK_SCORE = 1.0

    static String testText = """
FINAL DIAGNOSIS: 
A) Ileocecal valve, colon, polyp: 
    - Colonic mucosa with a small well-circumscribed lymphoid aggregate. 
B) Transverse colon polyp: 
    - Adenomatous polyp. 
C) Sigmoid colon: 
    - Hyperplastic polyp. 
    - Tubular adenoma.
D) Sigmod colon: 
    - Hyperplystic polyp. 
    - Tubluar adinoma.
    """

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void exactDictionaryTest() {
        MapDictionary<String> dictionary = new MapDictionary<String>()
        dictionary.addEntry(new DictionaryEntry<String>('adenoma',            'C0206677', CHUNK_SCORE))
        dictionary.addEntry(new DictionaryEntry<String>('tubular adenoma',    'C1112503', CHUNK_SCORE))
        dictionary.addEntry(new DictionaryEntry<String>('sigmoid colon',      'C0227391', CHUNK_SCORE))
        dictionary.addEntry(new DictionaryEntry<String>('hyperplastic polyp', 'C0333983', CHUNK_SCORE))
        
        ExactDictionaryChunker dictionaryChunkerTT = new ExactDictionaryChunker(dictionary,
                OpenNLPTokenizerFactory.INSTANCE,
                true,  // all matches (overlapping)
                false)  // case sensitive

        Chunking chunking = dictionaryChunkerTT.chunk(testText)
        assert chunking.chunkSet().size() == 4

        chunking.chunkSet().each { Chunk chunk ->
            int start = chunk.start()
            int end = chunk.end()
            String type = chunk.type()
            double score = chunk.score()
            String phrase = testText.substring(start,end)
            println "phrase=|${phrase}| start=${start} end=${end} type=${type} score=${score}"
        }
    }

    @Test
    public void approxDictionaryTest() {
        TrieDictionary<String> dict = new TrieDictionary<String>()
        dict.addEntry(new DictionaryEntry<String>('adenoma',            'C0206677'))
        dict.addEntry(new DictionaryEntry<String>('tubular adenoma',    'C1112503'))
        dict.addEntry(new DictionaryEntry<String>('sigmoid colon',      'C0227391'))
        dict.addEntry(new DictionaryEntry<String>('hyperplastic polyp', 'C0333983'))
        
        println "Dictionary: ${dict.toString()}\n"

        WeightedEditDistance editDistance = new FixedWeightEditDistance(0,-1,-1,-1,Double.NaN)

        double maxDistance = 2.0

        ApproxDictionaryChunker chunker = new ApproxDictionaryChunker(dict, 
            OpenNLPTokenizerFactory.INSTANCE,
            ////IndoEuropeanTokenizerFactory.INSTANCE,
            editDistance, 
            maxDistance)
        Chunking chunking = chunker.chunk(testText)
        CharSequence cs = chunking.charSequence()
        Set<Chunk> chunkSet = chunking.chunkSet()
        assert chunkSet.size() == 12
        
        printf("%15s  %15s   %8s\n",
                "Matched Phrase",
                "Dict Entry",
                "Distance")
        chunkSet.each { Chunk chunk ->
            int start = chunk.start()
            int end = chunk.end()
            CharSequence str = cs.subSequence(start,end)
            double distance = chunk.score()
            String match = chunk.type()
            printf("%15s  %15s   %8.1f\n", str, match, distance)
        }
        println '\n'
    }
}