package org.northshore.cbri.dict.lingpipe

import opennlp.tools.tokenize.TokenizerME
import opennlp.tools.tokenize.TokenizerModel
import opennlp.tools.util.Span

import com.aliasi.tokenizer.Tokenizer
import com.aliasi.tokenizer.TokenizerFactory

class OpenNLPTokenizerFactory implements TokenizerFactory {
    class OpenNLPTokenizer extends Tokenizer {

        TokenizerME tokenizer;
        String text;
        Span[] tokenSpans;
        char[] ch;
        int currTokenSpan = 0;
        
        public OpenNLPTokenizer(char[] ch, int start, int length) {
            InputStream modelIn = this.class.getResourceAsStream('/models/en-token.bin')
            TokenizerModel model = new TokenizerModel(modelIn)
            tokenizer = new TokenizerME(model)
            this.text = String.valueOf(ch, start, length)
            this.tokenSpans = tokenizer.tokenizePos(this.text)
        }
        
        @Override
        public int lastTokenStartPosition() {
            // TODO Auto-generated method stub
            return super.lastTokenStartPosition();
        }

        @Override
        public String nextToken() {
            if (currTokenSpan >= this.tokenSpans.length) { return null; }
            Span span = this.tokenSpans[currTokenSpan++]
            return this.text.substring(span.getStart(), span.getEnd())
        }
    }
    
    static public INSTANCE = new OpenNLPTokenizerFactory()

    @Override
    public Tokenizer tokenizer(char[] ch, int start, int length) {
        return new OpenNLPTokenizer(ch, start, length);
    }
}
