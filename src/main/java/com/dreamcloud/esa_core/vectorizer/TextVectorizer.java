package com.dreamcloud.esa_core.vectorizer;

public interface TextVectorizer {
    DocumentScoreVector vectorize(String text) throws Exception;
}
