package com.dreamcloud.esa_core.analyzer;

import java.util.Set;

/**
 * Tokenization options for the Lucene analyzers.
 */
public class AnalyzerOptions {
    public boolean porterStemmer = false;
    public int porterStemmerDepth = 1;
    public int minimumWordLength = 0;
    public int maximumWordLength = 0;
    public boolean lowerCase = false;
    public boolean classic = false;
    public boolean asciiFolding = false;
    public boolean singularCase = false;

    public StopWordRepository stopWordsRepository;
    public StopWordRepository rareWordsRepository;
    public DictionaryRepository dictionaryRepository;
    public Set<String> stopTokenTypes;
    public TokenizerFactory tokenizerFactory;

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

    public StopWordRepository getStopWordsRepository() {
        return stopWordsRepository;
    }

    public void setStopWordsRepository(StopWordRepository stopWordsRepository) {
        this.stopWordsRepository = stopWordsRepository;
    }

    public StopWordRepository getRareWordsRepository() {
        return rareWordsRepository;
    }

    public void setRareWordsRepository(StopWordRepository rareWordsRepository) {
        this.rareWordsRepository = rareWordsRepository;
    }

    public DictionaryRepository getDictionaryRepository() {
        return dictionaryRepository;
    }

    public void setDictionaryRepository(DictionaryRepository dictionaryRepository) {
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
}
