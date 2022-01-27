package com.dreamcloud.esa_core.similarity;

import com.dreamcloud.esa_core.vectorizer.DocumentScoreVector;
import com.dreamcloud.esa_core.vectorizer.TextVectorizer;

public class DocumentSimilarity {
    TextVectorizer vectorizer;

    public DocumentSimilarity(TextVectorizer vectorizer) {
        this.vectorizer = vectorizer;
    }

    private SimilarityInfo score(String doc1, String doc2) throws Exception {
        return score(doc1, doc2, false);
    }

    private SimilarityInfo score(String doc1, String doc2, boolean gatherTopConcepts) throws Exception {
        DocumentScoreVector doc1Vector = vectorizer.vectorize(doc1);
        DocumentScoreVector doc2Vector = vectorizer.vectorize(doc2);
        return doc1Vector.dotProduct(doc2Vector, gatherTopConcepts);
    }
}
