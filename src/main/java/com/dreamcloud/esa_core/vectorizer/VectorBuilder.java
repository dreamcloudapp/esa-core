package com.dreamcloud.esa_core.vectorizer;

import com.dreamcloud.esa_core.documentPreprocessor.DocumentPreprocessor;
import com.dreamcloud.esa_core.vectorizer.scoreMod.ScoreMod;
import com.dreamcloud.esa_core.vectorizer.scoreMod.ScoreModApplication;
import com.dreamcloud.esa_core.vectorizer.scoreMod.ScoreModPosition;
import com.dreamcloud.esa_score.analysis.CollectionInfo;
import com.dreamcloud.esa_score.analysis.TfIdfAnalyzer;
import com.dreamcloud.esa_score.score.DocumentScoreReader;
import com.dreamcloud.esa_score.score.TfIdfScore;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class VectorBuilder implements DocumentScoreVectorBuilder {
    public static Map<String, DocumentScoreVector> cache = new ConcurrentHashMap<>();
    DocumentScoreReader scoreReader;
    TfIdfAnalyzer tfIdfAnalyzer;
    VectorizationOptions vectorizationOptions;
    DocumentPreprocessor preprocessor;
    CollectionInfo collectionInfo;
    private ArrayList<ScoreMod> scoreMods = new ArrayList<>();

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

    public void addScoreMod(ScoreMod scoreMod) {
        this.scoreMods.add(scoreMod);
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

            Vector<TfIdfScore> scoreDocs = new Vector<>();

            //Score Mods

            //Pre-vectorization, term mods
            ArrayList<ScoreMod> preTermMods = new ArrayList<>();
            for (ScoreMod scoreMod: scoreMods) {
                if (scoreMod.getPosition() == ScoreModPosition.PRE_VECTORIZATION && scoreMod.getApplication() == ScoreModApplication.TERM) {
                    preTermMods.add(scoreMod);
                }
            }
            if (!preTermMods.isEmpty()) {
                for (String term: terms) {
                    Vector<TfIdfScore> termScores = new Vector<>();
                    scoreReader.getTfIdfScores(term, termScores);
                    for (ScoreMod scoreMod: preTermMods) {
                        termScores = scoreMod.applyMod(termScores);
                    }
                    scoreDocs.addAll(termScores);
                }
            } else {
                scoreReader.getTfIdfScores(terms, scoreDocs);
            }

            //Pre-vectorization, document mods
            ArrayList<ScoreMod> preDocMods = new ArrayList<>();
            for (ScoreMod scoreMod: scoreMods) {
                if (scoreMod.getPosition() == ScoreModPosition.PRE_VECTORIZATION && scoreMod.getApplication() == ScoreModApplication.DOCUMENT) {
                    preDocMods.add(scoreMod);
                }
            }
            if (!preDocMods.isEmpty()) {
                for (ScoreMod scoreMod: preTermMods) {
                    scoreDocs = scoreMod.applyMod(scoreDocs);
                }
            }

            //Vectorize and apply weights
            for (TfIdfScore docScore: scoreDocs) {
                double weight = scoreMap.get(docScore.getTerm());
                docScore.normalizeScore(weight);
                if (vectorizationOptions != null) {
                    vector.addScore(docScore.getDocument(), (float) docScore.getScore(), vectorizationOptions.commonConceptMultiplier);
                } else {
                    vector.addScore(docScore.getDocument(), (float) docScore.getScore());
                }
            }

            //Post-vectorization, document mod (no such thing as post/term mod)
            ArrayList<ScoreMod> postDocMods = new ArrayList<>();
            for (ScoreMod scoreMod: scoreMods) {
                if (scoreMod.getPosition() == ScoreModPosition.POST_VECTORIZATION && scoreMod.getApplication() == ScoreModApplication.DOCUMENT) {
                    postDocMods.add(scoreMod);
                }
            }
            if (!postDocMods.isEmpty()) {
                Vector<TfIdfScore> tfIdfScoreVector = new Vector<>();
                for (Integer documentId: vector.getDocumentScores().keySet()) {
                    tfIdfScoreVector.add(new TfIdfScore(documentId, null, vector.getScore(documentId)));
                    tfIdfScoreVector.sort((t1, t2) -> Float.compare((float) t2.getScore(), (float) t1.getScore()));
                }
                for (ScoreMod scoreMod: postDocMods) {
                    tfIdfScoreVector = scoreMod.applyMod(tfIdfScoreVector);
                }
                vector.getDocumentScores().clear();
                for (TfIdfScore tfIdfScore: tfIdfScoreVector) {
                    vector.addScore(tfIdfScore.getDocument(), (float) tfIdfScore.getScore());
                }
            }

            //return vector;
            cache.put(originalDocument, vector);
        }
        return cache.get(originalDocument);
    }
}
