package com.dreamcloud.esa_core.vectorizer;

import org.apache.commons.collections4.MultiValuedMap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BackrubVectorBuilder implements DocumentScoreVectorBuilder {
    private final DocumentScoreVectorBuilder vectorBuilder;
    private final MultiValuedMap<Integer, Integer> linkMap;

    public BackrubVectorBuilder(DocumentScoreVectorBuilder vectorBuilder,  MultiValuedMap<Integer, Integer> linkMap) {
        this.vectorBuilder = vectorBuilder;
        this.linkMap = linkMap;
    }

    @Override
    public DocumentScoreVector build(String document) throws Exception {
        DocumentScoreVector vector = vectorBuilder.build(document);
        //Make more general documents weigh more, if they have links pointing to them from within the vector
        Set<Integer> documentIds = vector.getDocumentScores().keySet();
        for (Integer documentId: documentIds) {
            Collection<Integer> incomingLinkIds = linkMap.get(documentId);
            int documentGenerality = incomingLinkIds.size();
            int lessGeneralLinks = 0;
            for (Integer incomingLinkId: incomingLinkIds) {
                int incomingGenerality = linkMap.get(incomingLinkId).size();
                if (documentIds.contains(incomingLinkId) && (Math.log10(documentGenerality) - Math.log10(incomingGenerality) > 1)) {
                    lessGeneralLinks++;
                }
            }
            float score = vector.getScore(documentId);
            vector.setScore(documentId, (float) (score + (0.5 * lessGeneralLinks)));
        }
        return vector;
    }
}
