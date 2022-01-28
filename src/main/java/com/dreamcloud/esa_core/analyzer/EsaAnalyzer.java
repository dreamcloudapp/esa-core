package com.dreamcloud.esa_core.analyzer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.TypeTokenFilter;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;

import java.io.IOException;

public class EsaAnalyzer extends Analyzer {
    AnalyzerOptions options;

    public EsaAnalyzer(AnalyzerOptions options) {
        this.options = options;
    }

    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer source = options.getTokenizerFactory().getTokenizer();
        TokenStream result = source;

        if (options.getStopTokenTypes() != null) {
            result = new TypeTokenFilter(source, options.getStopTokenTypes());
        }
        if (options.isUsingAsciiFolding()) {
            result = new ASCIIFoldingFilter(result, false);
        }
        if (options.isUsingLowerCase()) {
            result = new LowerCaseFilter(result);
        }
        if (options.isUsingClassic()) {
            result = new ClassicFilter(result);
        }
        if (options.isUsingSingularCase()) {
            result = new EnglishMinimalStemFilter(result);
        }

        if (options.getMinimumWordLength() > 0 || options.getMaximumWordLength() > 0) {
            int minimumWordLength = 0;
            int maximumWordLength = 1024;
            if (options.getMinimumWordLength() > 0) {
                minimumWordLength = options.getMinimumWordLength();
            }
            if (options.getMaximumWordLength() > 0) {
                maximumWordLength = options.getMaximumWordLength();
            }
            result = new LengthFilter(result, minimumWordLength, maximumWordLength);
        }
        if (options.getDictionaryRepository() != null) {
            result = new DictionaryFilter(result, options.getDictionaryRepository());
        }

        if (options.getStopWordsRepository() != null) {
            try {
                result = new StopFilter(result, options.getStopWordsRepository().getStopWords());
            } catch (IOException e) {
                System.out.println("ESA warning: failed to load stop word dictionary; " + e.getMessage());
                System.exit(1);
            }
        }

        if (options.getRareWordsRepository() != null) {
            try {
                result = new StopFilter(result, options.getRareWordsRepository().getStopWords());
            } catch (IOException e) {
                System.out.println("ESA warning: failed to load stop word dictionary; " + e.getMessage());
                System.exit(1);
            }
        }

        if (options.isUsingPorterStemmer()) {
            for(int i=0; i<options.getPorterStemmerDepth(); i++) {
                result = new PorterStemFilter(result);
            }
        }

        return new TokenStreamComponents(source, result);
    }
}
