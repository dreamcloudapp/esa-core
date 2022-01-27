package com.dreamcloud.esa_core.documentPreprocessor;

public class StandardPreprocessor implements DocumentPreprocessor {
    public String process(String document) {
        return document.replaceAll("[+\\-&|!(){}\\[\\]^\"~*?:;,/\\\\]+", " ");
    }

    public String getInfo() {
        return this.getClass().getSimpleName();
    }
}
