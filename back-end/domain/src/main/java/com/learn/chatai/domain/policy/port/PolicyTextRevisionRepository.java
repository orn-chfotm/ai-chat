package com.learn.chatai.domain.policy.port;

import com.learn.chatai.domain.policy.entity.PolicyTextRevision;

import java.util.List;
import java.util.Optional;

public interface PolicyTextRevisionRepository {

    PolicyTextRevision save(PolicyTextRevision revision);

    List<PolicyTextRevision> findByDocumentId(String documentId);

    Optional<PolicyTextRevision> findLatestByDocumentId(String documentId);
}
