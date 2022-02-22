package com.dreamcloud.esa_core.vectorizer;

public interface DocumentScoreVectorBuilder {
    DocumentScoreVector build(String document) throws Exception;
}
