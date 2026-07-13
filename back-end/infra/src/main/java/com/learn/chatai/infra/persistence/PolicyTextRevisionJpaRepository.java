package com.learn.chatai.infra.persistence;

import com.learn.chatai.domain.policy.entity.PolicyTextRevision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface PolicyTextRevisionJpaRepository extends JpaRepository<PolicyTextRevision, String> {

    List<PolicyTextRevision> findByDocumentIdOrderByCreatedAtDesc(String documentId);

    Optional<PolicyTextRevision> findFirstByDocumentIdOrderByCreatedAtDesc(String documentId);
}
