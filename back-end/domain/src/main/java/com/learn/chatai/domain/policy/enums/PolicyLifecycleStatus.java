package com.learn.chatai.domain.policy.enums;

/**
 * Shared status vocabulary for both {@code PolicyDocument} and {@code PolicyVersion}
 * (root CLAUDE.md "Document Lifecycle"). Document-level transitions used in this milestone
 * stop at REVIEWING/FAILED; EDITING through ARCHIVED are driven by later milestones
 * (text-edit API, vectorization/apply flow).
 */
public enum PolicyLifecycleStatus {
    UPLOADED,
    EXTRACTING,
    REVIEWING,
    EDITING,
    READY_TO_APPLY,
    VECTORIZING,
    ACTIVE,
    ARCHIVED,
    FAILED
}
