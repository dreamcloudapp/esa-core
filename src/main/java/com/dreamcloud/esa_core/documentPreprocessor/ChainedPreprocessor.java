package com.dreamcloud.esa_core.documentPreprocessor;

import java.util.ArrayList;

public class ChainedPreprocessor implements DocumentPreprocessor {
    ArrayList<DocumentPreprocessor> preprocessors;
    public ChainedPreprocessor(ArrayList<DocumentPreprocessor> preprocessors) {
        this.preprocessors = preprocessors;
    }

    public ChainedPreprocessor() {
        this.preprocessors = new ArrayList<>();
    }

    public void addPreprocessor(DocumentPreprocessor preprocessor) {
        this.preprocessors.add(0, preprocessor);
    }

    public String process(String document) throws Exception {
        for (DocumentPreprocessor preprocessor: preprocessors) {
            document = preprocessor.process(document);
        }
        return document;
    }

    public String getInfo() {
        ArrayList<String> preprocessorNames = new ArrayList<>();
        for (DocumentPreprocessor preprocessor: preprocessors) {
            preprocessorNames.add(preprocessor.getInfo());
        }
        return String.join(", ", preprocessorNames);
    }
}
