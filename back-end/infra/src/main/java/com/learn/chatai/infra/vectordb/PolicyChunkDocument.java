package com.learn.chatai.infra.vectordb;

import com.learn.chatai.domain.policy.enums.PolicyLifecycleStatus;
import com.learn.chatai.domain.policy.port.PolicyVectorStorePort.PolicyChunk;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Elasticsearch-facing shape of {@link PolicyChunk} (Jackson needs a mutable bean). Kept private
 * to {@code infra.vectordb} so the index/document schema can change without touching the domain
 * port.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class PolicyChunkDocument {

    private String chunkId;
    private String documentId;
    private String versionId;
    private String policyCode;
    private String sectionPath;
    private String content;
    private float[] embedding;
    private String status;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    static PolicyChunkDocument from(PolicyChunk chunk) {
        return new PolicyChunkDocument(
                chunk.chunkId(),
                chunk.documentId(),
                chunk.versionId(),
                chunk.policyCode(),
                chunk.sectionPath(),
                chunk.content(),
                chunk.embedding(),
                chunk.status().name(),
                chunk.effectiveFrom(),
                chunk.effectiveTo()
        );
    }

    PolicyChunk toDomain() {
        return new PolicyChunk(
                chunkId,
                documentId,
                versionId,
                policyCode,
                sectionPath,
                content,
                embedding,
                PolicyLifecycleStatus.valueOf(status),
                effectiveFrom,
                effectiveTo
        );
    }
}
