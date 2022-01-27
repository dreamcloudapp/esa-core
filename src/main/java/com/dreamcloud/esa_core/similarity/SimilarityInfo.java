package com.dreamcloud.esa_core.similarity;

import java.util.ArrayList;

public class SimilarityInfo {
    public float score;
    public ArrayList<String> topConcepts;

    public SimilarityInfo(float score, ArrayList<String> topConcepts) {
        this.score = score;
        this.topConcepts = topConcepts;
    }

    public SimilarityInfo(float score) {
        this(score, new ArrayList<>());
    }
}
