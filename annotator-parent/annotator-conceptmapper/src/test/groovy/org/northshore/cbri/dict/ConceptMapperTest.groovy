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
    public void createPipelineUIMAfit() {
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
                'TokenizerDescriptorPath', '/descriptors/OffsetTokenizer.xml',
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
                .getResource('/descriptors/OffsetTokenizer.xml')))

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
	
	@Test
	public void testOrderIndependentLookup() {
		// concept mapper
		AnalysisEngineDescription conceptMapper = AnalysisEngineFactory.createEngineDescription(
				org.apache.uima.conceptMapper.ConceptMapper,
				'caseMatch', 'ignoreall',
				'ResultingAnnotationName', 'org.apache.uima.conceptMapper.DictTerm',
				'AttributeList', ['canonical', 'AttributeType', 'AttributeValue', 'SemClass'] as String[],
				'FeatureList', ['DictCanon', 'AttributeType', 'AttributeValue', 'SemClass'] as String[],
				'SearchStrategy', 'SkipAnyMatch',
				'OrderIndependentLookup', true,
				'PrintDictionary', true,
				'FindAllMatches', false,
				'ReplaceCommaWithAND', false,
				'LanguageID', 'en',
				'TokenizerDescriptorPath', '/descriptors/OffsetTokenizer.xml',
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
				.getResource('/descriptors/OffsetTokenizer.xml')))

		AggregateBuilder builder = new AggregateBuilder()
		builder.add(offsetTokenizer)
		builder.add(conceptMapper)

		AnalysisEngine pipeline = builder.createAggregate()
		JCas jcas = pipeline.newJCas()
		UIMAUtil.jcas = jcas
		
		jcas.setDocumentText('papillary squamous cell carcinoma in situ.')
		pipeline.process(jcas)
		Collection<DictTerm> terms = UIMAUtil.select(type:DictTerm)
		terms.each { println "DictTerm: ${it.coveredText}: [${it.attributeType}, ${it.attributeValue}]" }
		assert terms.size() == 1

		jcas.reset()
		jcas.setDocumentText('situ papillary cell foo carcinoma bar squamous in.')
		pipeline.process(jcas)
		terms = UIMAUtil.select(type:DictTerm)
		terms.each { println "DictTerm: ${it.coveredText}: [${it.attributeType}, ${it.attributeValue}]" }
		assert terms.size() == 1

		jcas.reset()
		jcas.setDocumentText('The patient has carcinoma in situ, of the papillary squamous cell type.')
		pipeline.process(jcas)
		terms = UIMAUtil.select(type:DictTerm)
		terms.each { println "DictTerm: ${it.coveredText}: [${it.attributeType}, ${it.attributeValue}]" }
		assert terms.size() == 1

		jcas.reset()
		jcas.setDocumentText("cell blah type foo squamous bar papillary wtf situ hello in bye carcinoma")
		pipeline.process(jcas)
		terms = UIMAUtil.select(type:DictTerm)
		terms.each { println "DictTerm: ${it.coveredText}: [${it.attributeType}, ${it.attributeValue}]" }
		assert terms.size() == 1
	}
}
