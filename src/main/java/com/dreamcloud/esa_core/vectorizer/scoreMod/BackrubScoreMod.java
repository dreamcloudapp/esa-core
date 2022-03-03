package com.dreamcloud.esa_core.vectorizer.scoreMod;

import com.dreamcloud.esa_score.score.DocumentNameResolver;
import com.dreamcloud.esa_score.score.TfIdfScore;
import org.apache.commons.collections4.MultiValuedMap;

import javax.print.attribute.standard.DocumentName;
import javax.swing.text.Document;
import java.util.*;

public class BackrubScoreMod extends ScoreMod {
    private final MultiValuedMap<Integer, Integer> linkMap;

    public BackrubScoreMod(MultiValuedMap<Integer, Integer> linkMap) {
        super(ScoreModPosition.PRE_VECTORIZATION, ScoreModApplication.TERM);
        this.linkMap = linkMap;
    }

    @Override
    public Vector<TfIdfScore> applyMod(Vector<TfIdfScore> scores) {
        double averageScore = 0;

        Map<Integer, TfIdfScore> documentScores = new HashMap<>();
        for (TfIdfScore score: scores) {
            documentScores.put(score.getDocument(), score);
            averageScore += score.getScore();
        }
        averageScore /= scores.size();

        double averageInVectorLinks = 0;
        int backrubDocCount = 0;
        Map<Integer, Integer> inVectorLinkMap = new HashMap<>();

        for (Integer documentId: documentScores.keySet()) {
            Collection<Integer> incomingLinkIds = linkMap.get(documentId);
            int inVectorLinks = 0;
            for (Integer incomingLinkId: incomingLinkIds) {
                if (documentScores.containsKey(incomingLinkId) && Math.log10(1 + linkMap.get(documentId).size()) > Math.log10(1 + linkMap.get(incomingLinkId).size()) + 1) {
                    //System.out.println(DocumentNameResolver.getTitle(incomingLinkId) + " -> " + DocumentNameResolver.getTitle(documentId));
                    inVectorLinks++;
                    backrubDocCount++;
                }
            }
            inVectorLinkMap.put(documentId, inVectorLinks);
            averageInVectorLinks += inVectorLinks;
        }
        if (averageInVectorLinks > 0) {
            averageInVectorLinks /= backrubDocCount;
        }

        for (Integer documentId: documentScores.keySet()) {
            double inVectorLinks = inVectorLinkMap.get(documentId);
            if (inVectorLinks > 0) {
                //Adjust to fit score scale
                inVectorLinks = (inVectorLinks / (averageInVectorLinks / averageScore));

                TfIdfScore score = documentScores.get(documentId);
                double normalScore = score.getScore();
                double backrubScore = (0.8 * Math.log(1 + inVectorLinks) + 3.2 * Math.log(1 + normalScore)) * ((inVectorLinks - Math.max(0, inVectorLinks - normalScore)) / inVectorLinks);
                score.setScore(normalScore + backrubScore);
            }
        }

        return scores;
    }
}
