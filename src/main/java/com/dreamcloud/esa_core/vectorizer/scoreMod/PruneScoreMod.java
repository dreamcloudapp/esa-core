package com.dreamcloud.esa_core.vectorizer.scoreMod;

import com.dreamcloud.esa_core.vectorizer.VectorizationOptions;
import com.dreamcloud.esa_score.score.TfIdfScore;

import java.util.Vector;

public class PruneScoreMod extends ScoreMod {
    private final VectorizationOptions options;

    public PruneScoreMod(VectorizationOptions options) {
        super(ScoreModPosition.PRE_VECTORIZATION, ScoreModApplication.TERM);
        this.options = options;
    }

    @Override
    public Vector<TfIdfScore> applyMod(Vector<TfIdfScore> termScores) {
        Vector<TfIdfScore> allTermScores = new Vector<>();
        for (int scoreIdx = 0; scoreIdx < termScores.size(); scoreIdx++) {
            allTermScores.add(termScores.get(scoreIdx));
            if (scoreIdx + options.getWindowSize() < termScores.size()) {
                float headScore = (float) termScores.get(scoreIdx).getScore();
                float tailScore = (float) termScores.get(scoreIdx + options.getWindowSize()).getScore();
                if (headScore - tailScore < headScore * options.getWindowDrop()) {
                    break;
                }
            }
        }
        return allTermScores;
    }
}
