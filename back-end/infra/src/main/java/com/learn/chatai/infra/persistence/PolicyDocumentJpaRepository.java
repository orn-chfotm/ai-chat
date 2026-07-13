package com.learn.chatai.infra.persistence;

import com.learn.chatai.domain.policy.entity.PolicyDocument;
import org.springframework.data.jpa.repository.JpaRepository;

interface PolicyDocumentJpaRepository extends JpaRepository<PolicyDocument, String> {
}
