package com.learn.chatai.infra.persistence;

import com.learn.chatai.domain.policy.entity.PolicyDocument;
import com.learn.chatai.domain.policy.port.PolicyDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class PolicyDocumentRepositoryAdapter implements PolicyDocumentRepository {

    private final PolicyDocumentJpaRepository jpaRepository;

    @Override
    public PolicyDocument save(PolicyDocument document) {
        return jpaRepository.save(document);
    }

    @Override
    public Optional<PolicyDocument> findById(String documentId) {
        return jpaRepository.findById(documentId);
    }
}
