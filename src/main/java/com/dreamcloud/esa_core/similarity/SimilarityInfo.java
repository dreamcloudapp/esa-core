package com.dreamcloud.esa_core.similarity;

import java.util.ArrayList;

public class SimilarityInfo {
    private float score;
    private  ArrayList<String> topConcepts;

    public SimilarityInfo(float score, ArrayList<String> topConcepts) {
        this.score = score;
        this.topConcepts = topConcepts;
    }

    public SimilarityInfo(float score) {
        this(score, new ArrayList<>());
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public ArrayList<String> getTopConcepts() {
        return topConcepts;
    }

    public void setTopConcepts(ArrayList<String> topConcepts) {
        this.topConcepts = topConcepts;
    }
}
