package com.learn.chatai.infra.vectordb;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.learn.chatai.domain.policy.enums.PolicyLifecycleStatus;
import com.learn.chatai.domain.policy.port.PolicyVectorStorePort;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Elasticsearch implementation of {@link PolicyVectorStorePort} (PRD 04). Index creation/mapping
 * (the {@code embedding} field must be mapped as {@code dense_vector}) is provisioned separately
 * before this milestone goes live — this adapter only wires the read/write contract so callers
 * can depend on the port today.
 */
@Component
public class ElasticsearchPolicyVectorStoreAdapter implements PolicyVectorStorePort {

    private static final String INDEX_NAME = "policy-chunks";

    private final ElasticsearchClient client;

    public ElasticsearchPolicyVectorStoreAdapter(ElasticsearchClient client) {
        this.client = client;
    }

    @Override
    public void indexChunks(List<PolicyChunk> chunks) {
        if (chunks.isEmpty()) {
            return;
        }
        try {
            var response = client.bulk(b -> {
                for (PolicyChunk chunk : chunks) {
                    b.operations(op -> op.index(idx -> idx
                            .index(INDEX_NAME)
                            .id(chunk.chunkId())
                            .document(PolicyChunkDocument.from(chunk))));
                }
                return b;
            });
            if (response.errors()) {
                String failedIds = chunks.stream().map(PolicyChunk::chunkId).collect(Collectors.joining(", "));
                throw new IllegalStateException("Elasticsearch bulk indexing reported errors for chunks: " + failedIds);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to index policy chunks into Elasticsearch", e);
        }
    }

    @Override
    public void deleteByVersionId(String versionId) {
        try {
            client.deleteByQuery(d -> d
                    .index(INDEX_NAME)
                    .query(q -> q.term(t -> t.field("versionId").value(versionId))));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to delete policy chunks for versionId=" + versionId, e);
        }
    }

    @Override
    public List<PolicyChunkMatch> searchActiveChunks(String policyCode, float[] queryEmbedding, int topK) {
        try {
            List<Float> vector = new ArrayList<>(queryEmbedding.length);
            for (float value : queryEmbedding) {
                vector.add(value);
            }

            SearchResponse<PolicyChunkDocument> response = client.search(s -> s
                    .index(INDEX_NAME)
                    .knn(k -> k
                            .field("embedding")
                            .queryVector(vector)
                            .k(topK)
                            .numCandidates(Math.max(topK * 10, 50))
                            .filter(f -> f.bool(bq -> bq
                                    .filter(tf -> tf.term(t -> t.field("policyCode").value(policyCode)))
                                    .filter(tf -> tf.term(t -> t.field("status").value(PolicyLifecycleStatus.ACTIVE.name())))))),
                    PolicyChunkDocument.class);

            return response.hits().hits().stream()
                    .map(hit -> new PolicyChunkMatch(hit.source().toDomain(), hit.score() == null ? 0.0 : hit.score()))
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to search policy chunks in Elasticsearch", e);
        }
    }
}
