package com.dreamcloud.esa_core.documentPreprocessor;

public interface DocumentPreprocessor {
    String process(String document) throws Exception;
    String getInfo();
}
