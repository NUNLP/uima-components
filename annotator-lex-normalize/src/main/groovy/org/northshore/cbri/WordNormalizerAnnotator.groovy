package org.northshore.cbri

import static org.northshore.cbri.UIMAUtil.*
import gov.nih.nlm.nls.gspell.Candidate
import gov.nih.nlm.nls.gspell.GSpellLite
import gov.nih.nlm.nls.lvg.Api.NormApi
import groovy.util.logging.Log4j

import org.apache.ctakes.typesystem.type.syntax.WordToken
import org.apache.uima.UimaContext
import org.apache.uima.analysis_engine.AnalysisEngineProcessException
import org.apache.uima.fit.component.JCasAnnotator_ImplBase
import org.apache.uima.fit.descriptor.ConfigurationParameter
import org.apache.uima.jcas.JCas
import org.apache.uima.resource.ResourceInitializationException

@Log4j
class WordNormalizerAnnotator extends JCasAnnotator_ImplBase {
    public static final String PARAM_LEXICON_NAME = "lexiconName"
    
    @ConfigurationParameter(name = "lexiconName", mandatory = true)
    private String lexiconName;
    
    NormApi normalizer;
    GSpellLite gspell;

    @Override
    public void initialize(UimaContext context) throws ResourceInitializationException {
        super.initialize(context)
        this.normalizer = new NormApi()
        URL url = WordNormalizerAnnotator.class.getResource("/" + this.lexiconName)
        File file = new File(url.toURI())
        if (file.exists()) { log.info(file.getAbsolutePath()) }
        this.gspell = new GSpellLite(file, GSpellLite.READ_ONLY)
    }

    @Override
    public void process(JCas aJCas) throws AnalysisEngineProcessException {
        UIMAUtil.jcas = aJCas
        Collection<WordToken> tokens = select(type:WordToken)

        tokens.each { WordToken token ->
            // determine the spelling suggestion for the token
            if (! this.gspell.isSpelledCorrect(token.coveredText)) {
                Candidate[] candidates = gspell.find(token.coveredText)
                if (candidates) {
                    List<Candidate> sorted = candidates.sort { -it.getRank() }
                    token.setSuggestion(sorted[0].getName())
                    this.gspell.freeCandidates()
                }
            }
            // determine the normalized form of the token
            Vector<String> normsters = this.normalizer.Mutate(token.suggestion ?: token.coveredText)
            if (normsters.size() == 1) {
                token.setNormalizedForm(normsters[0])
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy()
        this.normalizer.CleanUp()
    }
}
