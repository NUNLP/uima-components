package org.northshore.cbri
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine
import static org.apache.uima.fit.util.JCasUtil.selectSingle
import static org.junit.Assert.*
import groovy.util.logging.Log4j

import java.lang.reflect.Type

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.fit.factory.JCasFactory
import org.apache.uima.fit.testing.factory.TokenBuilder
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.google.common.io.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

@Log4j
class DictionaryAnnotatorTest {

    @BeforeClass
    public static void setupClass() {
        BasicConfigurator.configure()
    }
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void smokeTest() throws Exception {
        AnalysisEngine ae = createEngine(DictionaryAnnotator,
                DictionaryAnnotator.PARAM_MODEL_LOCATION, "src/test/resources/dict/test-dict.txt")

        JCas jcas = JCasFactory.createJCas()
        TokenBuilder<BaseToken, Sentence> tb = new TokenBuilder<BaseToken, Sentence>(BaseToken, Sentence)
        tb.buildTokens(jcas, "There is a tubular adenoma in the sigmoid colon .")

        ae.process(jcas)
        UIMAUtil.jcas = jcas
        Collection<IdentifiedAnnotation> idAnns = UIMAUtil.select(type:IdentifiedAnnotation)
        assertEquals 2, idAnns.size()
        assertEquals 'tubular adenoma', idAnns[0].coveredText
        assertEquals 'sigmoid colon', idAnns[1].coveredText
    }

    @Test
    public void testParseDictionary() {
        GsonBuilder builder = new GsonBuilder()
        Gson gson = builder.create()

        // load in the dictionary file
        URL url = Resources.getResource("dict/test-dict.txt")
        Type collectionType = new TypeToken<HashMap<String, String>>(){}.getType();
        
        new File(url.toURI()).withReader { reader ->
            // each line is a dictionary entry
            String line = null
            while ((line = reader.readLine()) != null) { 
                Map<String, String> map = gson.fromJson(line, collectionType);
                log.info map.get("phrase")
            }
        }        
    }
    
    @Test
    public void testAhoCorasickDictionary() {
        String text1 = "sigmoid colon"; String sem1 = "SC"
        String text2 = "tubular adenoma"; String sem2 = "TA"
        AhoCorasickDict aho = new AhoCorasickDict()
        aho.put(text1, sem1)
        aho.put(text2, sem2)
        assertEquals ("sigmoid::2::SC||| colon:: ", aho.find(text1))
        assertEquals ("tubular::2::TA||| adenoma:: ", aho.find(text2))
        assertEquals ("foo:: ", aho.find("foo"))
    }
}
