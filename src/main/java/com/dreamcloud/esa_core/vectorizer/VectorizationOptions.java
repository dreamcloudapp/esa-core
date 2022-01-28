package com.dreamcloud.esa_core.vectorizer;

public class VectorizationOptions {
    protected int windowSize = 0;
    protected float windowDrop = 0;
    protected int vectorLimit = 0;
    protected float commonConceptMultiplier = 1.0f;

    public int getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(int windowSize) {
        this.windowSize = windowSize;
    }

    public float getWindowDrop() {
        return windowDrop;
    }

    public void setWindowDrop(float dropOff) {
        this.windowDrop = dropOff;
    }

    public int getVectorLimit() {
        return vectorLimit;
    }

    public void setVectorLimit(int vectorLimit) {
        this.vectorLimit = vectorLimit;
    }

    public float getCommonConceptMultiplier() {
        return commonConceptMultiplier;
    }

    public void setCommonConceptMultiplier(float commonConceptMultiplier) {
        this.commonConceptMultiplier = commonConceptMultiplier;
    }
}
