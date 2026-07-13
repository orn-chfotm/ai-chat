package com.learn.chatai.infra.persistence;

import com.learn.chatai.domain.policy.entity.PolicyTextRevision;
import com.learn.chatai.domain.policy.port.PolicyTextRevisionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
class PolicyTextRevisionRepositoryAdapter implements PolicyTextRevisionRepository {

    private final PolicyTextRevisionJpaRepository jpaRepository;

    @Override
    public PolicyTextRevision save(PolicyTextRevision revision) {
        return jpaRepository.save(revision);
    }

    @Override
    public List<PolicyTextRevision> findByDocumentId(String documentId) {
        return jpaRepository.findByDocumentIdOrderByCreatedAtDesc(documentId);
    }

    @Override
    public Optional<PolicyTextRevision> findLatestByDocumentId(String documentId) {
        return jpaRepository.findFirstByDocumentIdOrderByCreatedAtDesc(documentId);
    }
}
