package com.dreamcloud.esa_core.vectorizer;

public class Vectorizer implements TextVectorizer {
    protected DocumentScoreVectorBuilder builder;

    public Vectorizer(DocumentScoreVectorBuilder builder) {
        this.builder = builder;
    }

    public DocumentScoreVector vectorize(String text) throws Exception {
        return builder.build(text);
    }
}
