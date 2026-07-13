package com.learn.chatai.domain.policy.entity;

import com.learn.chatai.core.BaseTimeEntity;
import com.learn.chatai.domain.policy.enums.PolicyLifecycleStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Applicable version unit (PRD 02). Schema/entity only in this milestone — nothing populates
 * this table yet. The READY_TO_APPLY -> VECTORIZING -> ACTIVE/ARCHIVED transition (and the
 * "only one ACTIVE version per policyCode" invariant) is built in the vectorization/apply milestone.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "policy_version")
public class PolicyVersion extends BaseTimeEntity {

    @Id
    @Column(name = "version_id", length = 64)
    private String versionId;

    @Column(name = "policy_code", nullable = false, length = 100)
    private String policyCode;

    @Column(name = "document_id", nullable = false, length = 64)
    private String documentId;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PolicyLifecycleStatus status;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;
}
