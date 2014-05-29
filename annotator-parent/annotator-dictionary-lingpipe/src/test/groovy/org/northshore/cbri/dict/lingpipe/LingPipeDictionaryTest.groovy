package org.northshore.cbri.dict.lingpipe

import static org.junit.Assert.*

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.aliasi.chunk.Chunk
import com.aliasi.chunk.Chunking
import com.aliasi.dict.DictionaryEntry
import com.aliasi.dict.ExactDictionaryChunker
import com.aliasi.dict.MapDictionary
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
    - Tubular adenoma .
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
        dictionary.addEntry(new DictionaryEntry<String>('adenoma',         'C0206677', CHUNK_SCORE))
        dictionary.addEntry(new DictionaryEntry<String>('tubular adenoma', 'C1112503', CHUNK_SCORE))
        dictionary.addEntry(new DictionaryEntry<String>('sigmoid colon',   'C0227391', CHUNK_SCORE))
        
        ExactDictionaryChunker dictionaryChunkerTT = new ExactDictionaryChunker(dictionary,
                                     IndoEuropeanTokenizerFactory.INSTANCE,
                                     true,  // all matches (overlapping)
                                     true)  // case sensitive
        
        Chunking chunking = dictionaryChunkerTT.chunk(testText)
        for (Chunk chunk : chunking.chunkSet()) {
            int start = chunk.start()
            int end = chunk.end()
            String type = chunk.type()
            double score = chunk.score()
//            String phrase = text.substring(start,end)
//            System.out.println("     phrase=|" + phrase + "|"
//                               + " start=" + start
//                               + " end=" + end
//                               + " type=" + type
//                               + " score=" + score);
        }
    }
}
