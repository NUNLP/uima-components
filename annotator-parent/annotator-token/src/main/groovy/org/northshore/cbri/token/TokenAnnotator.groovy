package org.northshore.cbri.token

import static org.northshore.cbri.dsl.UIMAUtil.*
import groovy.util.logging.Log4j
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.tools.util.Span
import opennlp.uima.tokenize.TokenizerModelResource

import org.apache.commons.lang.StringUtils
import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.syntax.WordToken
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineDescription
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.fit.descriptor.ExternalResource
import org.apache.uima.fit.factory.AnalysisEngineFactory
import org.apache.uima.fit.factory.ExternalResourceFactory
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.apache.uima.resource.ResourceAccessException
import org.apache.uima.resource.ResourceInitializationException
import org.codehaus.groovy.control.CompilerConfiguration
import org.northshore.cbri.dsl.UIMAUtil

@Log4j
public final class TokenAnnotator extends JCasAnnotator_ImplBase {
    public static final String PARAM_CONTAINER_TYPE = 'containerTypeName'
    @ConfigurationParameter(name = 'containerTypeName', mandatory = false, 
        defaultValue = 'org.apache.uima.jcas.tcas.DocumentAnnotation')
    private String containerTypeName;

    public static final String PARAM_POST_PROCESS_SCRIPT_FILE = 'postProcessScriptFileName'
    @ConfigurationParameter(name = 'postProcessScriptFileName', mandatory = false)
    private String postProcessScriptFileName;

    public static final String TOKEN_MODEL_KEY = 'token_model'
    @ExternalResource(key = 'token_model')
    TokenizerModelResource modelResource;
    
    private Script postProcessScript;
    private TokenizerME tokenizer;
    private Class<Annotation> containerType;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context)       
        try {
            this.containerType = Class.forName(this.containerTypeName)
        }
        catch (Exception e) {
            throw new ResourceInitializationException(e)
        }
        
        TokenizerModel model
        try {
            model = modelResource.getModel()
        } catch (ResourceAccessException e) {
            throw new ResourceInitializationException(e)
        }
        this.tokenizer = new TokenizerME(model)

        // initialize post-processing script
        if (this.postProcessScriptFileName != null) {
            CompilerConfiguration config = new CompilerConfiguration()
            config.setScriptBaseClass('org.northshore.cbri.dsl.UIMAUtil')
            GroovyShell shell = new GroovyShell(config)
            try {
                String scriptContents = ''
                scriptContents = this.getClass().getResource(this.postProcessScriptFileName).text
                this.postProcessScript = shell.parse(scriptContents)
            } catch (IOException e) {
                throw new ResourceInitializationException()
            }
        }
    }

    @Override
    public void destroy() {
        tokenizer = null
    }

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        UIMAUtil.jcas = aJCas
        select(type:(this.containerType)).each {
            Span[] tokenSpans = tokenizer.tokenizePos(it.coveredText)
            int offset = it.getBegin()
            tokenSpans.each { Span span ->
                Class annType = BaseToken
                int begin = offset + span.getStart()
                int end = offset + span.getEnd()
                String text = it.coveredText.substring(span.getStart(), span.getEnd())
                if (StringUtils.isAlpha(text)) { annType = WordToken }
                create(type:annType, begin:begin, end:end)
            }
        }

        if (this.postProcessScript != null) {
            this.postProcessScript.run()
        }
    }

    public static void main(String[] args) {
        AnalysisEngineDescription desc = AnalysisEngineFactory.createEngineDescription(
                TokenAnnotator)
        ExternalResourceFactory.createDependencyAndBind(desc,
                TokenAnnotator.TOKEN_MODEL_KEY,
                opennlp.uima.tokenize.TokenizerModelResourceImpl,
                'file:models/en-token.bin')
        PrintWriter writer = new PrintWriter(new File(args[0]))
        desc.toXML(writer)
        writer.close()
    }
}
