package com.dreamcloud.esa_core.analyzer;

import org.apache.lucene.analysis.Tokenizer;

public abstract class TokenizerFactory {
    abstract public Tokenizer getTokenizer();
}
