package com.dreamcloud.esa_core.vectorizer.scoreMod;

import com.dreamcloud.esa_score.score.TfIdfScore;

import java.util.Vector;

abstract public class ScoreMod {
    private final ScoreModPosition position;
    private final ScoreModApplication application;

    public ScoreMod(ScoreModPosition position, ScoreModApplication application) {
        if (position == ScoreModPosition.PRE_VECTORIZATION && application == ScoreModApplication.TERM) {
            throw new IllegalArgumentException("Mods that apply to terms must be pre-vectorization.");
        }

        this.position = position;
        this.application = application;
    }

    public ScoreModApplication getApplication() {
        return application;
    }

    public ScoreModPosition getPosition() {
        return position;
    }

    abstract public Vector<TfIdfScore> applyMod(Vector<TfIdfScore> scores);
}
