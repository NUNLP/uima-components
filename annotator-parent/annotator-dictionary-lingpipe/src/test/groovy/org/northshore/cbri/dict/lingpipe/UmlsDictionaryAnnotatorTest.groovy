package org.northshore.cbri.dict.lingpipe
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine
import static org.apache.uima.fit.util.JCasUtil.selectSingle
import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.textsem.AnatomicalSiteMention
import org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.factory.JCasFactory
import org.apache.uima.jcas.JCas
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.northshore.cbri.dsl.UIMAUtil
import org.northshore.cbri.token.TokenAnnotator

@Log4j
class UmlsDictionaryAnnotatorTest {
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
    AnalysisEngine tokenizer;
    JCas jcas;

    @BeforeClass
    public static void setupClass() {
        BasicConfigurator.configure()
    }

    @Before
    public void setUp() throws Exception {
        AnalysisEngineDescription tokenDesc = AnalysisEngineFactory.createEngineDescription(
                TokenAnnotator)
        ExternalResourceFactory.createDependencyAndBind(tokenDesc,
                TokenAnnotator.TOKEN_MODEL_KEY,
                opennlp.uima.tokenize.TokenizerModelResourceImpl,
                "file:models/en-token.bin")

        this.tokenizer = AnalysisEngineFactory.createEngine(tokenDesc)
        this.jcas = JCasFactory.createJCas()
        this.jcas.setDocumentText(this.testText)
        UIMAUtil.JCas = this.jcas
        UIMAUtil.create(type:Sentence, begin:0, end:this.testText.length()-1)
        tokenizer.process(jcas)
    }

    @After
    public void tearDown() throws Exception {
    }
    
    @Test void createTypeTest() {
        Class cls = "org.apache.ctakes.typesystem.type.textsem.DiseaseDisorderMention" as Class
        Object instance = cls.newInstance(jcas)
        assert instance != null
    }

    @Test
    public void testExactMatch() {
        AnalysisEngineDescription dictDesc = AnalysisEngineFactory.createEngineDescription(
                UmlsDictionaryAnnotator,
                UmlsDictionaryAnnotator.PARAM_DICTIONARY_FILE, "/dict/test-umls-dict-auto.txt",
                UmlsDictionaryAnnotator.PARAM_MAX_DISTANCE, 0)
        ExternalResourceFactory.createDependencyAndBind(dictDesc,
                TokenAnnotator.TOKEN_MODEL_KEY,
                opennlp.uima.tokenize.TokenizerModelResourceImpl,
                "file:models/en-token.bin")
        AnalysisEngine dictionary = AnalysisEngineFactory.createEngine(dictDesc)
        dictionary.process(jcas)

        // test results
        Collection<IdentifiedAnnotation> idAnns = UIMAUtil.select(type:IdentifiedAnnotation)
        idAnns.each { println "Identified Annotation: [${it.coveredText}]" }
        assertEquals 4, idAnns.size()
        
        assertEquals 'Adenomatous polyp',       idAnns[0].coveredText
        assertEquals 'C0206677',                idAnns[0].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[0].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[0].class
        
        assertEquals 'Sigmoid colon',           idAnns[1].coveredText
        assertEquals 'C0227391',                idAnns[1].ontologyConcepts[0].cui
        assertEquals 'T023',                    idAnns[1].ontologyConcepts[0].tui
        assertEquals AnatomicalSiteMention,     idAnns[1].class
        
        assertEquals 'Hyperplastic polyp',      idAnns[2].coveredText
        assertEquals 'C0333983',                idAnns[2].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[2].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[2].class
        
        assertEquals 'Tubular adenoma',         idAnns[3].coveredText
        assertEquals 'C0334292',                idAnns[3].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[3].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[3].class
    }
    
    @Test
    public void testApproxMatch() {
        AnalysisEngineDescription dictDesc = AnalysisEngineFactory.createEngineDescription(
                UmlsDictionaryAnnotator,
                UmlsDictionaryAnnotator.PARAM_DICTIONARY_FILE, "/dict/test-umls-dict-auto.txt",
                UmlsDictionaryAnnotator.PARAM_MAX_DISTANCE, 1)
        ExternalResourceFactory.createDependencyAndBind(dictDesc,
                TokenAnnotator.TOKEN_MODEL_KEY,
                opennlp.uima.tokenize.TokenizerModelResourceImpl,
                "file:models/en-token.bin")
        AnalysisEngine dictionary = AnalysisEngineFactory.createEngine(dictDesc)
        dictionary.process(jcas)

        // test results
        Collection<IdentifiedAnnotation> idAnns = UIMAUtil.select(type:IdentifiedAnnotation)
        idAnns.each { println "Identified Annotation: [${it.coveredText}]" }
        assertEquals 9, idAnns.size()
        
        assertEquals 'Adenomatous polyp.',      idAnns[0].coveredText
        assertEquals 'C0206677',                idAnns[0].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[0].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[0].class
        
        assertEquals 'Adenomatous polyp',       idAnns[1].coveredText
        assertEquals 'C0206677',                idAnns[1].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[1].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[1].class
        
        assertEquals 'Sigmoid colon:',          idAnns[2].coveredText
        assertEquals 'C0227391',                idAnns[2].ontologyConcepts[0].cui
        assertEquals 'T023',                    idAnns[2].ontologyConcepts[0].tui
        assertEquals AnatomicalSiteMention,     idAnns[2].class
        
        assertEquals 'Sigmoid colon',           idAnns[3].coveredText
        assertEquals 'C0227391',                idAnns[3].ontologyConcepts[0].cui
        assertEquals 'T023',                    idAnns[3].ontologyConcepts[0].tui
        assertEquals AnatomicalSiteMention,     idAnns[3].class
        
        assertEquals 'Hyperplastic polyp.',     idAnns[4].coveredText
        assertEquals 'C0333983',                idAnns[4].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[4].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[4].class
        
        assertEquals 'Hyperplastic polyp',      idAnns[5].coveredText
        assertEquals 'C0333983',                idAnns[5].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[5].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[5].class
        
        assertEquals 'Hyperplastic polyp',      idAnns[6].coveredText
        assertEquals 'C0333983',                idAnns[6].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[6].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[6].class
        
        assertEquals 'Tubular adenoma',         idAnns[7].coveredText
        assertEquals 'C0334292',                idAnns[7].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[7].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[7].class
        
        assertEquals 'adenoma',                 idAnns[8].coveredText
        assertEquals 'C0001430',                idAnns[8].ontologyConcepts[0].cui
        assertEquals 'T191',                    idAnns[8].ontologyConcepts[0].tui
        assertEquals DiseaseDisorderMention,    idAnns[8].class
    }
}
