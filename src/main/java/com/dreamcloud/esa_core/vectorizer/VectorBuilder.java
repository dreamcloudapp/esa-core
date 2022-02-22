package com.dreamcloud.esa_core.vectorizer;

import com.dreamcloud.esa_core.documentPreprocessor.DocumentPreprocessor;
import com.dreamcloud.esa_score.analysis.CollectionInfo;
import com.dreamcloud.esa_score.analysis.TfIdfAnalyzer;
import com.dreamcloud.esa_score.score.DocumentScoreReader;
import com.dreamcloud.esa_score.score.TfIdfScore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class VectorBuilder implements DocumentScoreVectorBuilder {
    public static Map<String, DocumentScoreVector> cache = new ConcurrentHashMap<>();
    DocumentScoreReader scoreReader;
    TfIdfAnalyzer tfIdfAnalyzer;
    VectorizationOptions vectorizationOptions;
    DocumentPreprocessor preprocessor;
    CollectionInfo collectionInfo;

    public VectorBuilder(DocumentScoreReader scoreReader, CollectionInfo collectionInfo, TfIdfAnalyzer analyzer, DocumentPreprocessor preprocessor, VectorizationOptions vectorizationOptions) {
        this.scoreReader = scoreReader;
        this.tfIdfAnalyzer = analyzer;
        this.collectionInfo = collectionInfo;
        this.preprocessor = preprocessor;
        this.vectorizationOptions = vectorizationOptions;
    }

    public VectorBuilder(DocumentScoreReader scoreReader, CollectionInfo collectionInfo, TfIdfAnalyzer tfIdfAnalyzer, DocumentPreprocessor preprocessor) {
        this(scoreReader, collectionInfo, tfIdfAnalyzer, preprocessor, null);
    }

    public DocumentScoreVector build(String document) throws Exception {
        String originalDocument = document;
        if (!cache.containsKey(originalDocument)) {
            if (preprocessor != null) {
                document = preprocessor.process(document);
            }
            DocumentScoreVector vector = new DocumentScoreVector();
            TfIdfScore[] scores = tfIdfAnalyzer.getTfIdfScores(document);
            Map<String, Float> scoreMap = new HashMap<>();
            String[] terms = new String[scores.length];
            int i = 0;
            for (TfIdfScore score: scores) {
                terms[i++] = score.getTerm();
                scoreMap.put(score.getTerm(), (float) score.getScore());
            }

            //We need to prune these scores!
            Vector<TfIdfScore> scoreDocs = new Vector<>();
            if (vectorizationOptions != null && vectorizationOptions.windowSize > 0) {
                Vector<TfIdfScore> allTermScores = new Vector<>();
                for (String term: terms) {
                    Vector<TfIdfScore> termScores = new Vector<>();
                    scoreReader.getTfIdfScores(term, termScores);
                    for (int scoreIdx = 0; scoreIdx < termScores.size(); scoreIdx++) {
                        allTermScores.add(termScores.get(scoreIdx));
                        if (scoreIdx + vectorizationOptions.windowSize < termScores.size()) {
                            float headScore = (float) termScores.get(scoreIdx).getScore();
                            float tailScore = (float) termScores.get(scoreIdx + vectorizationOptions.windowSize).getScore();
                            if (headScore - tailScore < headScore * vectorizationOptions.getWindowDrop()) {
                                break;
                            }
                        }
                    }
                }
                scoreDocs = allTermScores;
            } else {
                scoreReader.getTfIdfScores(terms, scoreDocs);
            }

            for (TfIdfScore docScore: scoreDocs) {
                double weight = scoreMap.get(docScore.getTerm());
                docScore.normalizeScore(weight);
                if (vectorizationOptions != null) {
                    vector.addScore(docScore.getDocument(), (float) docScore.getScore(), vectorizationOptions.commonConceptMultiplier);
                } else {
                    vector.addScore(docScore.getDocument(), (float) docScore.getScore());
                }
            }

            if (vectorizationOptions != null && vectorizationOptions.vectorLimit > 0) {
                TfIdfScore[] sortedScores = new TfIdfScore[vector.getDocumentScores().size()];
                int s = 0;
                for (Integer documentId: vector.getDocumentScores().keySet()) {
                    sortedScores[s++] = new TfIdfScore(documentId, null, vector.getScore(documentId));
                }

                Arrays.sort(sortedScores, (t1, t2) -> Float.compare((float) t2.getScore(), (float) t1.getScore()));
                vector.getDocumentScores().clear();
                int cutOff = Math.min(vectorizationOptions.vectorLimit, sortedScores.length);
                for (int t=0; t<cutOff; t++) {
                    vector.addScore(sortedScores[t].getDocument(), (float) sortedScores[t].getScore());
                }
            }

            //return vector;
            cache.put(originalDocument, vector);
        }
        return cache.get(originalDocument);
    }
}
