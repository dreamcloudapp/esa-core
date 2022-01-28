package com.dreamcloud.esa_core.analyzer;

import com.dreamcloud.esa_core.documentPreprocessor.DocumentPreprocessor;

import java.util.Set;

/**
 * Analysis options for documents.
 *
 * These are mostly flags for various Lucene filters.
 * You can of course use any analyzer that you want and don't need to rely on the analyzer or
 * options provided here.
 */
public class AnalyzerOptions {
    protected boolean porterStemmer = false;
    protected int porterStemmerDepth = 1;
    protected int minimumWordLength = 0;
    protected int maximumWordLength = 0;

    //filters
    protected boolean lowerCase = false;
    protected boolean classic = false;
    protected boolean asciiFolding = false;
    protected boolean singularCase = false;

    protected DocumentPreprocessor preprocessor;

    protected FilterWordRepository stopWordsRepository;
    protected FilterWordRepository dictionaryRepository;
    protected Set<String> stopTokenTypes;
    protected TokenizerFactory tokenizerFactory;

    public AnalyzerOptions() {}

    public boolean isUsingPorterStemmer() {
        return porterStemmer;
    }

    public void setUsingPorterStemmer(boolean porterStemmer) {
        this.porterStemmer = porterStemmer;
    }

    public int getPorterStemmerDepth() {
        return porterStemmerDepth;
    }

    public void setPorterStemmerDepth(int porterStemmerDepth) {
        this.porterStemmerDepth = porterStemmerDepth;
    }

    public int getMinimumWordLength() {
        return minimumWordLength;
    }

    public void setMinimumWordLength(int minimumWordLength) {
        this.minimumWordLength = minimumWordLength;
    }

    public int getMaximumWordLength() {
        return maximumWordLength;
    }

    public void setMaximumWordLength(int maximumWordLength) {
        this.maximumWordLength = maximumWordLength;
    }

    public boolean isUsingLowerCase() {
        return lowerCase;
    }

    public void setUsingLowerCase(boolean lowerCase) {
        this.lowerCase = lowerCase;
    }

    public boolean isUsingClassic() {
        return classic;
    }

    public void setUsingClassic(boolean classic) {
        this.classic = classic;
    }

    public boolean isUsingAsciiFolding() {
        return asciiFolding;
    }

    public void setUsingAsciiFolding(boolean asciiFolding) {
        this.asciiFolding = asciiFolding;
    }

    public boolean isUsingSingularCase() {
        return singularCase;
    }

    public void setUsingSingularCase(boolean singularCase) {
        this.singularCase = singularCase;
    }

    public FilterWordRepository getStopWordsRepository() {
        return stopWordsRepository;
    }

    public void setStopWordsRepository(FilterWordRepository stopWordsRepository) {
        this.stopWordsRepository = stopWordsRepository;
    }

    public FilterWordRepository getDictionaryRepository() {
        return dictionaryRepository;
    }

    public void setFilterWordRepository(FilterWordRepository dictionaryRepository) {
        this.dictionaryRepository = dictionaryRepository;
    }

    public Set<String> getStopTokenTypes() {
        return stopTokenTypes;
    }

    public void setStopTokenTypes(Set<String> stopTokenTypes) {
        this.stopTokenTypes = stopTokenTypes;
    }

    public TokenizerFactory getTokenizerFactory() {
        return tokenizerFactory;
    }

    public void setTokenizerFactory(TokenizerFactory tokenizerFactory) {
        this.tokenizerFactory = tokenizerFactory;
    }

    public DocumentPreprocessor getPreprocessor() {
        return preprocessor;
    }

    public void setPreprocessor(DocumentPreprocessor preprocessor) {
        this.preprocessor = preprocessor;
    }
}
