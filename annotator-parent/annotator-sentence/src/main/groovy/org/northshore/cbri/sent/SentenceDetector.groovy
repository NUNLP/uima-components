package org.northshore.cbri.sent

import static org.northshore.cbri.dsl.UIMAUtil.*
import groovy.util.logging.Log4j

import java.util.regex.Matcher
import java.util.regex.Pattern

import opennlp.tools.sentdetect.SentenceDetectorME
import opennlp.tools.sentdetect.SentenceModel
import opennlp.tools.util.Span

import org.apache.ctakes.typesystem.type.textspan.Segment
import org.apache.ctakes.typesystem.type.textspan.Sentence
import org.apache.log4j.Level
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.jcas.tcas.Annotation
import org.apache.uima.resource.ResourceInitializationException
import org.codehaus.groovy.control.CompilationFailedException
import org.codehaus.groovy.control.CompilerConfiguration
import org.northshore.cbri.dsl.UIMAUtil

import com.google.common.base.Charsets
import com.google.common.io.Resources

@Log4j
class SentenceDetector extends JCasAnnotator_ImplBase {

    public static final String SD_MODEL_FILE_PARAM = 'sentenceModelFile'
    public static final String SD_SPLIT_PATTERN = 'splitPatternStr'
    public static final String SD_SEGMENTS_TO_PARSE = 'segmentsToParse'

    @ConfigurationParameter(name = 'sentenceModelFile', mandatory = true, description = 'File holding sentence model')
    private String sentenceModelFile;

    @ConfigurationParameter(name = 'segmentsToParse', mandatory = false, description = 'Script providing input segments')
    private String segmentsToParse;

    @ConfigurationParameter(name = 'splitPatternStr', mandatory = false, description = 'Characters to split on')
    private String splitPatternStr;

    private SentenceModel model
    private SentenceDetectorME sentDetect

    private Pattern splitPattern
    
    @Override
    public void initialize(UimaContext aContext) throws ResourceInitializationException {
        super.initialize(aContext)
        try {
            InputStream modelIn = Resources.getResource(sentenceModelFile).openStream();
            this.model = new SentenceModel(modelIn)
            this.sentDetect = new SentenceDetectorME(this.model)

            if (this.splitPatternStr) {
                this.splitPattern = Pattern.compile(this.splitPatternStr)
            }
        } catch (Exception ace) {
            throw new ResourceInitializationException(ace);
        }
    }

    @Override
    public void process(JCas jcas) throws AnalysisEngineProcessException {
        log.setLevel(Level.INFO)
        UIMAUtil.setJCas(jcas)
        Collection<Segment> segs;
        try {
            if (segmentsToParse != null) {
                CompilerConfiguration config = new CompilerConfiguration()
                config.setScriptBaseClass("org.northshore.cbri.dsl.UIMAUtil")
                GroovyShell shell = new GroovyShell(config)
                URL url = Resources.getResource(this.segmentsToParse)
                String scriptContents = Resources.toString(url, Charsets.UTF_8)
                segs = (Collection<Segment>)shell.evaluate(scriptContents)
            }
            else {
                segs = select(type:Segment)
            }
        } catch (CompilationFailedException|IOException e) {
            log.error(e.stackTrace)
        }
        segs.each { Segment seg ->
            Span[] spans = this.sentDetect.sentPosDetect(seg.coveredText)
            spans.each { Span span ->
                this.splitAndAdjustSpan(span, seg).each { Span subspan ->
                    create(type:Sentence, begin:subspan.start, end:subspan.end)
                }
            }
        }
    }

    private Collection<Span> splitAndAdjustSpan(Span span, Annotation cover) {
        List<Span> spans = []

        String coveredText = cover.coveredText
        if (this.splitPattern == null) {
            Tuple trimOffsets = this.trimOffsets(coveredText.substring(span.start, span.end))
            addSpan(spans, new Span(cover.begin+span.start+trimOffsets[0], cover.begin+span.end-trimOffsets[1]))
            return spans
        }
        int offset = cover.begin + span.start
        Matcher matcher = this.splitPattern.matcher(coveredText.substring(span.start, span.end))
        while(matcher.find()) {
            int newOffset = cover.begin + span.start + matcher.end(0)
            Tuple trimOffsets = this.trimOffsets(coveredText.substring(offset-cover.begin, newOffset-cover.begin))
            addSpan(spans, new Span(offset+trimOffsets[0], newOffset-trimOffsets[1]))
            ////log.info "Creating Span[${offset+trimOffsets[0]}, ${newOffset-trimOffsets[1]}]"
            offset = newOffset
        }
        Tuple trimOffsets = this.trimOffsets(coveredText.substring(offset-cover.begin, span.end))
        addSpan(spans, new Span(offset+trimOffsets[0], cover.begin+span.end-trimOffsets[1]))

        return spans;
    }
    
    private addSpan(Collection<Span> spans, Span span) {
        if (span.start != span.end) {
            spans.add(span)
        }
    }

    private Tuple trimOffsets(String text) {
        int beginTrimOffset, endTrimOffset = 0
        if (text.isAllWhitespace()) {
            return [0, text.length()]
        }
        for (c in text) {
            if (c.isAllWhitespace()) {
                beginTrimOffset += 1
            }
            else break
        }
        for (c in text.reverse()) {
            if (c.isAllWhitespace()) {
                endTrimOffset += 1
            }
            else break
        }
        return [beginTrimOffset, endTrimOffset]
    }
}
