package com.learn.chatai.domain.policy.port;

import com.learn.chatai.domain.policy.entity.PolicyDocument;

import java.util.Optional;

public interface PolicyDocumentRepository {

    PolicyDocument save(PolicyDocument document);

    Optional<PolicyDocument> findById(String documentId);
}
