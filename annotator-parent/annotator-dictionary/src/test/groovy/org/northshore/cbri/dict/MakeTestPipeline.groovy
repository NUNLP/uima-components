package org.northshore.cbri.dict

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.uima.tokenize.TokenizerModelResourceImpl

import org.apache.uima.analysis_engine.AnalysisEngine
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.fit.factory.AggregateBuilder
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory
import org.apache.uima.resource.ExternalResourceDescription
import org.apache.uima.resource.ResourceInitializationException
import org.apache.uima.resource.metadata.TypeSystemDescription
import org.apache.uima.util.InvalidXMLException
import org.northshore.cbri.dict.phrase.DictionaryModel;
import org.northshore.cbri.dict.phrase.DictionaryModelFactory;
import org.northshore.cbri.dict.phrase.DictionaryModelPool;
import org.northshore.cbri.dsl.GroovyAnnotator
import org.northshore.cbri.sent.SentenceDetector
import org.northshore.cbri.token.TokenAnnotator
import org.northshore.cbri.dict.DictionaryAnnotator;
import org.xml.sax.SAXException

import com.fasterxml.jackson.databind.ObjectMapper

class MakeTestPipeline {
	static AggregateBuilder makePipeline() throws ResourceInitializationException,
	InvalidXMLException, IOException, SAXException {			
		
		// --------------------------------------------------------------------
		// Type system description
		// --------------------------------------------------------------------
		
		TypeSystemDescription tsd = TypeSystemDescriptionFactory.createTypeSystemDescription()
		tsd.resolveImports()


		// --------------------------------------------------------------------
		// External resources
		// --------------------------------------------------------------------

		ExternalResourceDescription tokenModelResDesc = ExternalResourceFactory.createExternalResourceDescription(
			TokenizerModelResourceImpl, "file:models/en-token.bin");
			
		// --------------------------------------------------------------------
		// analysis engines
		// --------------------------------------------------------------------
		
		AnalysisEngineDescription segmenter = AnalysisEngineFactory.createEngineDescription(GroovyAnnotator,
			GroovyAnnotator.PARAM_SCRIPT_FILE, 'groovy/SimpleSegmenter.groovy')
		
		AnalysisEngineDescription sentDetector = AnalysisEngineFactory.createEngineDescription(
				SentenceDetector,
				SentenceDetector.SD_SPLIT_PATTERN, /([\r\n]*\s+(?=-))|(:[\s\r\n])/,
				SentenceDetector.SD_MODEL_FILE_PARAM, 'models/sd-med-model.zip',
				SentenceDetector.SD_SEGMENTS_TO_PARSE, 'groovy/PathReportSegmentsToParse.groovy')

		AnalysisEngineDescription tokenizer = AnalysisEngineFactory.createEngineDescription(
				TokenAnnotator,
				TokenAnnotator.TOKEN_MODEL_KEY, tokenModelResDesc)

		AnalysisEngineDescription dict = AnalysisEngineFactory.createEngineDescription(
			DictionaryAnnotator,
			DictionaryAnnotator.PARAM_DICTIONARY_ID, 1)


		// --------------------------------------------------------------------
		// aggregate builder
		// --------------------------------------------------------------------
		
		AggregateBuilder builder = new AggregateBuilder()
		builder.add(segmenter)
		builder.add(sentDetector)
		builder.add(tokenizer)
		builder.add(dict)

		return builder
	}

	static void main(args) {
		TokenizerME tokenizer = new TokenizerME(new TokenizerModel(new File(this.class.getResource('/models/en-token.bin').file)))
		
		AbstractionSchema schema = new ObjectMapper().readValue(
			new File(this.class.getResource('/abstractionSchema/test-abstraction-schema.json').file),
			AbstractionSchema.class);
		
		DictionaryModel model = DictionaryModelFactory.make(schema, tokenizer)
		DictionaryModelPool.put(1, model)

		AggregateBuilder builder = makePipeline()
		AnalysisEngineDescription desc = builder.createAggregateDescription()
		PrintWriter writer = new PrintWriter(new File('src/test/resources/descriptors/TestPipeline.xml'))
		desc.toXML(writer)
		writer.close()
	}
}
