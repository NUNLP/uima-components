package org.northshore.cbri

import static org.northshore.cbri.UIMAUtil.*
import groovy.util.logging.Log4j
import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.tools.util.Span
import opennlp.uima.tokenize.TokenizerModelResource

import org.apache.commons.lang3.StringUtils
import org.apache.ctakes.typesystem.type.syntax.BaseToken
import org.apache.ctakes.typesystem.type.syntax.WordToken
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.fit.descriptor.ExternalResource
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceAccessException
import org.apache.uima.resource.ResourceInitializationException
import org.codehaus.groovy.control.CompilerConfiguration

import com.google.common.base.Charsets
import com.google.common.io.Resources


@Log4j
public final class TokenAnnotator extends JCasAnnotator_ImplBase {
    public static final String PARAM_POST_PROCESS_SCRIPT_FILE = "postProcessScriptFileName"
    @ConfigurationParameter(name = "postProcessScriptFileName", mandatory = false)
    private String postProcessScriptFileName
    
    public static final String TOKEN_MODEL_KEY = "token_model"
    @ExternalResource(key = "token_model")
    TokenizerModelResource modelResource

    private Script postProcessScript
    private TokenizerME tokenizer

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context)
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
            config.setScriptBaseClass("org.northshore.cbri.UIMAUtil")
            GroovyShell shell = new GroovyShell(config)
            try {
                String scriptContents = ""
                URL url = Resources.getResource(this.postProcessScriptFileName)
                scriptContents = Resources.toString(url, Charsets.UTF_8)
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
        select(type:Sentence).each { Sentence sentence ->
            Span[] tokenSpans = tokenizer.tokenizePos(sentence.getCoveredText())
            int sentenceOffset = sentence.getBegin()
            tokenSpans.each { Span span ->
                
                Class annType = BaseToken
                int begin = sentenceOffset + span.getStart()
                int end = sentenceOffset + span.getEnd()
                String text = sentence.coveredText.substring(span.getStart(), span.getEnd())
                if (StringUtils.isAlpha(text)) { annType = WordToken }
                create(type:annType, begin:begin, end:end)
            }
        }
        
        if (this.postProcessScript != null) {
            this.postProcessScript.run()
        }
    }
}
