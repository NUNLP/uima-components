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
                TokenAnnotator,
                TokenAnnotator.PARAM_POST_PROCESS_SCRIPT_FILE,
                "/groovy/TokenPostProcess.groovy")
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
    
    
}
