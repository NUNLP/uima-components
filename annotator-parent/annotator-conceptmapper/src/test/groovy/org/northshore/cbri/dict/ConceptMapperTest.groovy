package org.northshore.cbri.dict

import static org.junit.Assert.*
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.BasicConfigurator
import org.apache.log4j.Level
import org.apache.uima.UIMAFramework
import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.conceptMapper.DictTerm
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ExternalResourceDescription
import org.apache.uima.util.XMLInputSource
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import org.northshore.cbri.dsl.GroovyAnnotator
import org.northshore.cbri.dsl.UIMAUtil
import org.northshore.cbri.sent.SentenceDetector
import org.northshore.cbri.token.TokenAnnotator

@Log4j
class ConceptMapperTest {
    @BeforeClass
    public static void setupClass() {
        BasicConfigurator.configure()
    }

    @Before
    public void setUp() throws Exception {
        log.setLevel(Level.INFO)
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createEngineDescFile() {
        AnalysisEngineDescription desc = UIMAFramework
                .getXMLParser()
                .parseAnalysisEngineDescription(
                new XMLInputSource(
                ConceptMapperTest.class
                .getResource('/descriptors/aggregate/OffsetTokenizerMatcher.xml')))
        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
        assert engine != null
        JCas jcas = engine.newJCas()
        jcas.setDocumentText('Papillary squamous cell carcinoma in situ.')
        engine.process(jcas)
        UIMAUtil.jcas = jcas
        Collection<DictTerm> terms = UIMAUtil.select(type:DictTerm)
        terms.each { println "DictTerm: ${it.coveredText}: [${it.attributeType}, ${it.attributeValue}]" }
        assert terms.size() == 1
    }

    @Test
    public void creatPipelineUIMAfit() {
        // concept mapper
        AnalysisEngineDescription conceptMapper = AnalysisEngineFactory.createEngineDescription(
                org.apache.uima.conceptMapper.ConceptMapper,
                'caseMatch', 'ignoreall',
                'ResultingAnnotationName', 'org.apache.uima.conceptMapper.DictTerm',
                'AttributeList', ['canonical', 'AttributeType', 'AttributeValue', 'SemClass'] as String[],
                'FeatureList', ['DictCanon', 'AttributeType', 'AttributeValue', 'SemClass'] as String[],
                'SearchStrategy', 'ContiguousMatch',
                'OrderIndependentLookup', false,
                'PrintDictionary', false,
                'FindAllMatches', false,
                'ReplaceCommaWithAND', false,
                'LanguageID', 'en',
                'TokenizerDescriptorPath', '/descriptors/primitive/OffsetTokenizer.xml',
                'TokenAnnotation', 'uima.tt.TokenAnnotation',
                'SpanFeatureStructure', 'uima.tcas.DocumentAnnotation',
                'ResultingAnnotationName', 'org.apache.uima.conceptMapper.DictTerm',
                'ResultingEnclosingSpanName', 'enclosingSpan',
                'MatchedTokensFeatureName', 'matchedTokens',
                'ResultingAnnotationMatchedTextFeature', 'matchedText'
                )
        ExternalResourceFactory.createDependencyAndBind(conceptMapper,
                org.apache.uima.conceptMapper.ConceptMapper.PARAM_DICT_FILE,
                org.apache.uima.conceptMapper.support.dictionaryResource.DictionaryResource_impl,
                'file:dict/NAACCRSearchTerm.xml')
        assert conceptMapper != null

        // offset tokenizer
        AnalysisEngineDescription offsetTokenizer = UIMAFramework
                .getXMLParser()
                .parseAnalysisEngineDescription(
                new XMLInputSource(
                ConceptMapperTest.class
                .getResource('/descriptors/primitive/OffsetTokenizer.xml')))

        AggregateBuilder builder = new AggregateBuilder()
        builder.add(offsetTokenizer)
        builder.add(conceptMapper)

        AnalysisEngine pipeline = builder.createAggregate()
        JCas jcas = pipeline.newJCas()
        jcas.setDocumentText('Papillary squamous cell carcinoma in situ.')
        pipeline.process(jcas)
        UIMAUtil.jcas = jcas

        Collection<DictTerm> terms = UIMAUtil.select(type:DictTerm)
        terms.each { println "DictTerm: ${it.coveredText}: [${it.attributeType}, ${it.attributeValue}]" }
        assert terms.size() == 1

    }

    //    @Test
    //    public void createConceptMapperPipeline() {
    //        // segmenter
    //        AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
    //                GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/SimpleSegmenter.groovy')
    //
    //        // sentence detector
    //        AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
    //                SentenceDetector,
    //                SentenceDetector.SD_MODEL_FILE_PARAM, 'models/sd-med-model.zip')
    //
    //        // tokenizer
    //        AnalysisEngineDescription tokenizer = AnalysisEngineFactory.createEngineDescription(
    //                TokenAnnotator)
    //        ExternalResourceFactory.createDependencyAndBind(tokenizer,
    //                TokenAnnotator.TOKEN_MODEL_KEY,
    //                opennlp.uima.tokenize.TokenizerModelResourceImpl,
    //                "file:models/en-token.bin")
    //
    //        // offset tokenizer
    //        AnalysisEngineDescription offsetTokenizer = UIMAFramework
    //                .getXMLParser()
    //                .parseAnalysisEngineDescription(
    //                new XMLInputSource(
    //                ConceptMapperTest.class
    //                .getResource('/descriptors/primitive/OffsetTokenizer.xml')))
    //
    //        // concept mapper
    //        AnalysisEngineDescription conceptMapper = UIMAFramework
    //                .getXMLParser()
    //                .parseAnalysisEngineDescription(
    //                new XMLInputSource(
    //                ConceptMapperTest.class
    //                .getResource('/descriptors/primitive/ConceptMapper.xml')))
    //
    //        AggregateBuilder builder = new AggregateBuilder()
    //        builder.add(segmenter)
    //        builder.add(sentDetector)
    //        builder.add(tokenizer)
    //        builder.add(offsetTokenizer)
    //        builder.add(conceptMapper)
    //
    //        PrintWriter writer = new PrintWriter(new File('src/test/resources/descriptors/aggregate/ConceptMapperPipeline.xml'))
    //        def desc = builder.createAggregateDescription()
    //        desc.toXML(writer)
    //        writer.close()
    //
    //        AnalysisEngine pipeline = builder.createAggregate()
    //        JCas jcas = pipeline.newJCas()
    //        jcas.setDocumentText('Papillary squamous cell carcinoma in situ.')
    //        pipeline.process(jcas)
    //        UIMAUtil.jcas = jcas
    //
    //        Collection<Sentence> sents = UIMAUtil.select(type:Sentence)
    //        assert sents.size() == 1
    //
    //        Collection<BaseToken> tokens = UIMAUtil.select(type:BaseToken)
    //        assert tokens.size() == 7
    //
    //        Collection<DictTerm> terms = UIMAUtil.select(type:DictTerm)
    //        terms.each { println "DictTerm: ${it.coveredText}: [${it.attributeType}, ${it.attributeValue}]" }
    //        assert terms.size() == 1
    //    }

    //    @Test
    //    public void createOwnEngineUIMAfit() {
    //        ExternalResourceDescription extDesc = ExternalResourceFactory.createExternalResourceDescription(
    //                DictionaryModel, 'file:dict/Morphenglish.xml')
    //        assert extDesc != null
    //
    //        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
    //                org.northshore.cbri.dict.ConceptMapper,
    //                ConceptMapper.PARAM_DICT_FILE, 'dict/Morphenglish.xml',
    //                ConceptMapper.MODEL_KEY, extDesc
    //                )
    //        assert desc != null
    //
    //        AnalysisEngine engine = AnalysisEngineFactory.createEngine(desc)
    //        assert engine != null
    //
    //        JCas jcas = engine.newJCas()
    //        jcas.setDocumentText('The patient is Jane Doe. Papillary squamous cell carcinoma in situ.')
    //        engine.process(jcas)
    //    }
}
