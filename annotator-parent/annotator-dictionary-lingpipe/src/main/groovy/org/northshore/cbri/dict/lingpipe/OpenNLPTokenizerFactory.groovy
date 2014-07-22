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
        int currTokenPos = 0;
        
        public OpenNLPTokenizer(TokenizerModel model, char[] ch, int start, int length) {
            tokenizer = new TokenizerME(model)
            this.text = String.valueOf(ch, start, length)
            this.tokenSpans = tokenizer.tokenizePos(this.text)
        }
        
        @Override
        public int lastTokenStartPosition() {
            if (currTokenPos == 0) return null
            return this.tokenSpans[currTokenPos-1].start
        }
        
        @Override
        public int lastTokenEndPosition() {
            if (currTokenPos == 0) return null
            return this.tokenSpans[currTokenPos-1].end
        }

        @Override
        public String nextToken() {
            if (currTokenPos >= this.tokenSpans.length) { return null; }
            Span span = this.tokenSpans[currTokenPos++]
            return this.text.substring(span.getStart(), span.getEnd())
        }
    }
    
    TokenizerModel tokenModel;
    
    public OpenNLPTokenizerFactory(TokenizerModel model) {
        this.tokenModel = model;
    }

    @Override
    public Tokenizer tokenizer(char[] ch, int start, int length) {
        return new OpenNLPTokenizer(this.tokenModel, ch, start, length);
    }
}
