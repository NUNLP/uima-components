package org.northshore.cbri
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine
import static org.apache.uima.fit.util.JCasUtil.selectSingle
import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.factory.JCasFactory
import org.apache.uima.fit.testing.factory.TokenBuilder
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

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
    public void testLongestMatch() throws Exception {
        String text = """
FINAL DIAGNOSIS: A) Ileocecal valve, colon, polyp: - Colonic mucosa with a small well-circumscribed lymphoid aggregate. B) Transverse colon polyp: - Adenomatous polyp. C) Sigmoid colon: - Hyperplastic polyp. - Tubular adenoma .
"""
        AnalysisEngineDescription tokenDesc = AnalysisEngineFactory.createEngineDescription(
                TokenAnnotator,
                TokenAnnotator.PARAM_POST_PROCESS_SCRIPT_FILE,
                "/groovy/TokenPostProcess.groovy")
        ExternalResourceFactory.createDependencyAndBind(tokenDesc,
                TokenAnnotator.TOKEN_MODEL_KEY,
                opennlp.uima.tokenize.TokenizerModelResourceImpl,
                "file:models/en-token.bin")
        AnalysisEngine tokenizer = AnalysisEngineFactory.createEngine(tokenDesc)
        assert tokenizer != null

        AnalysisEngineDescription dictDesc = AnalysisEngineFactory.createEngineDescription(
                DictionaryAnnotator,
                DictionaryAnnotator.PARAM_DICTIONARY_FILE, "/dict/test-dict.txt")
        ExternalResourceFactory.createDependencyAndBind(dictDesc,
                TokenAnnotator.TOKEN_MODEL_KEY,
                opennlp.uima.tokenize.TokenizerModelResourceImpl,
                "file:models/en-token.bin")
        AnalysisEngine dictionary = AnalysisEngineFactory.createEngine(dictDesc)
        assert dictionary != null

        // set up tokens for dictionary lookup
        JCas jcas = JCasFactory.createJCas()
        jcas.setDocumentText(text)
        UIMAUtil.jcas = jcas
        UIMAUtil.create(type:Sentence, begin:0, end:text.length()-1)
        tokenizer.process(jcas)
        
        // process with dictionary
        dictionary.process(jcas)
        
        // test results
        Collection<IdentifiedAnnotation> idAnns = UIMAUtil.select(type:IdentifiedAnnotation)
        idAnns.each { println "Identified Annotation: [${it.coveredText}]" }
        assertEquals 3, idAnns.size()
        assertEquals 'Sigmoid colon', idAnns[0].coveredText
        assertEquals 'Tubular adenoma', idAnns[1].coveredText
        assertEquals 'adenoma', idAnns[2].coveredText
    }
}
