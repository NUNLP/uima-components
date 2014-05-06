package org.northshore.cbri

import groovy.util.logging.Log4j

import org.apache.log4j.Level
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException

@Log4j
class TimeExRecognizer extends JCasAnnotator_ImplBase {
    
    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext)
    }
    
    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        log.setLevel(Level.INFO)
    }
}
