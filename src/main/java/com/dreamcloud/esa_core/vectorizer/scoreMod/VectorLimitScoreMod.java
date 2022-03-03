package com.dreamcloud.esa_core.vectorizer.scoreMod;

import com.dreamcloud.esa_score.score.TfIdfScore;

import java.util.Vector;

public class VectorLimitScoreMod extends ScoreMod {
    private final int maxLength;

    public VectorLimitScoreMod(int maxLength) {
        super(ScoreModPosition.POST_VECTORIZATION, ScoreModApplication.DOCUMENT);
        this.maxLength = maxLength;
    }

    @Override
    public Vector<TfIdfScore> applyMod(Vector<TfIdfScore> scores) {
        scores.sort((t1, t2) -> Float.compare((float) t2.getScore(), (float) t1.getScore()));
        return new Vector<>(scores.subList(0, Math.min(scores.size(), maxLength)));
    }
}
