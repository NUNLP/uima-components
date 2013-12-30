package org.northshore.cbri

import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.apache.uima.resource.ResourceInitializationException

abstract class BratAnnotator extends JCasAnnotator_ImplBase {
	public static final String PARAM_ANN_FILE = "annFileName"

	@ConfigurationParameter(name = "annFileName", mandatory = true, description = "File holding BRAT annotations")
	private String annFileName

	@Override
	public void initialize(UimaContext context)
			throws ResourceInitializationException {
		super.initialize(context);
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		InputStream annIn = BratAnnotator.class.getResourceAsStream(this.annFileName)
		BratDocument doc = BratDocument.parseDocument("docId", txtIn, annIn)
	}
	
	public abstract Map<String, Annotation> annotationMap();
}
