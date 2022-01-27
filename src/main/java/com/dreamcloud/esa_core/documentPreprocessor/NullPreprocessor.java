package com.dreamcloud.esa_core.documentPreprocessor;

public class NullPreprocessor implements DocumentPreprocessor {
    public String process(String document) {
        return document;
    }

    public String getInfo() {
        return this.getClass().getSimpleName();
    }
}
