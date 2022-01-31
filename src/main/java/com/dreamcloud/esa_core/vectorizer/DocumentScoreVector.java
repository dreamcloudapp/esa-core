package com.dreamcloud.esa_core.vectorizer;

import com.dreamcloud.esa_core.similarity.SimilarityInfo;
import org.eclipse.collections.api.map.primitive.MutableObjectFloatMap;
import org.eclipse.collections.impl.factory.primitive.ObjectFloatMaps;

import java.util.Arrays;

public class DocumentScoreVector {
    MutableObjectFloatMap<Integer> documentScores = ObjectFloatMaps.mutable.empty();

    public DocumentScoreVector() {

    }

    public void merge(DocumentScoreVector other) {
        merge(other, 1.0f);
    }

    public void merge(DocumentScoreVector other, float commonConceptMultiplier) {
        for (int documentId: other.documentScores.keySet()) {
            this.addScore(documentId, other.getScore(documentId), commonConceptMultiplier);
        }
    }

    public SimilarityInfo dotProduct(DocumentScoreVector other) {
        return dotProduct(other, false);
    }

    public SimilarityInfo dotProduct(DocumentScoreVector other, boolean gatherTopConcepts) {
        MutableObjectFloatMap<Integer> theirScores = other.documentScores;
        MutableObjectFloatMap<Integer> topCommonConcepts = ObjectFloatMaps.mutable.empty();
        float norm1 = 0;
        float norm2 = 0;
        float dotProduct = 0;
        for (int documentId: documentScores.keySet()) {
            float ourScore = documentScores.get(documentId);
            if (theirScores.containsKey(documentId)) {
                float theirScore = theirScores.get(documentId);
                dotProduct += ourScore * theirScore;

                if (gatherTopConcepts) {
                    topCommonConcepts.put(documentId, ourScore * theirScore);
                }
            }
            norm1 += ourScore * ourScore;
        }
        for (int documentId: theirScores.keySet()) {
            float theirScore = theirScores.get(documentId);
            norm2 += theirScore * theirScore;
        }
        SimilarityInfo similarityInfo = new SimilarityInfo((float) (dotProduct / (Math.sqrt(norm1) * Math.sqrt((norm2)))));
        if (gatherTopConcepts) {
            Integer[] commonDocumentIds = topCommonConcepts.keySet().toArray(Integer[]::new);
            Arrays.sort(commonDocumentIds, (Integer c1, Integer c2) -> Float.compare(topCommonConcepts.get(c2), topCommonConcepts.get(c1)));
            for (int conceptIdx = 0; conceptIdx < 10; conceptIdx++) {
                similarityInfo.getTopConcepts().add(String.valueOf(commonDocumentIds[conceptIdx]));
            }
        }
        return similarityInfo;
    }

    public Integer[] getSortedDocumentIds() {
        return documentScores.keySet().toArray(new Integer[0]);
    }

    public void addScore(int document, float score) {
        if (score > 0) {
            documentScores.addToValue(document, score);
        }
    }

    public void addScore(int document, float score, float commonConceptMultiplier) {
        if (score > 0) {
            if (documentScores.containsKey(document)) {
                float currentScore = documentScores.get(document);
                documentScores.put(document, (currentScore + score) * commonConceptMultiplier);
            } else {
                documentScores.addToValue(document, score);
            }
        }
    }

    public float getScore(int documentId) {
        return documentScores.get(documentId);
    }
}
