package com.learn.chatai.domain.policy.port;

import com.learn.chatai.domain.policy.enums.PolicyLifecycleStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * Port implemented by {@code infra.vectordb} (PRD 04). Domain/service code depends on this
 * interface only, not on the concrete Vector DB product, per root CLAUDE.md
 * "Vector DB 저장 단위는 사람이 추적 가능한 ID를 가져야 하며 ... 역추적할 수 있어야 한다."
 * Embedding generation is a separate concern (Spring AI) — this port only stores/searches
 * already-embedded chunks.
 */
public interface PolicyVectorStorePort {

    void indexChunks(List<PolicyChunk> chunks);

    void deleteByVersionId(String versionId);

    List<PolicyChunkMatch> searchActiveChunks(String policyCode, float[] queryEmbedding, int topK);

    /**
     * One retrievable unit sent to/read from the Vector DB. Mirrors PRD 04 chunk metadata so
     * every stored chunk can be traced back to its {@code documentId}/{@code versionId} and
     * excluded once its {@code status} is no longer {@code ACTIVE}.
     */
    record PolicyChunk(
            String chunkId,
            String documentId,
            String versionId,
            String policyCode,
            String sectionPath,
            String content,
            float[] embedding,
            PolicyLifecycleStatus status,
            LocalDate effectiveFrom,
            LocalDate effectiveTo
    ) {
    }

    record PolicyChunkMatch(PolicyChunk chunk, double score) {
    }
}
