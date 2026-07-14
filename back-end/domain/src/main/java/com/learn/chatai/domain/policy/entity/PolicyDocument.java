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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "policy_document")
public class PolicyDocument extends BaseTimeEntity {

    @Id
    @Column(name = "document_id", length = 64)
    private String documentId;

    @Column(name = "policy_code", nullable = false, length = 100)
    private String policyCode;

    @Column(name = "original_file_name", nullable = false)
    private String originalFileName;

    @Column(name = "storage_key", nullable = false, length = 500)
    private String storageKey;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    @Column(name = "checksum", length = 128)
    private String checksum;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private PolicyLifecycleStatus status;

    private PolicyDocument(String documentId, String policyCode, String originalFileName, String storageKey,
                            String contentType, Long sizeBytes, String checksum) {
        this.documentId = documentId;
        this.policyCode = policyCode;
        this.originalFileName = originalFileName;
        this.storageKey = storageKey;
        this.contentType = contentType;
        this.sizeBytes = sizeBytes;
        this.checksum = checksum;
        this.status = PolicyLifecycleStatus.UPLOADED;
    }

    public static PolicyDocument uploaded(String documentId, String policyCode, String originalFileName,
                                           String storageKey, String contentType, Long sizeBytes, String checksum) {
        return new PolicyDocument(documentId, policyCode, originalFileName, storageKey, contentType, sizeBytes, checksum);
    }

    public void markExtracting() {
        this.status = PolicyLifecycleStatus.EXTRACTING;
    }

    public void markReviewing() {
        this.status = PolicyLifecycleStatus.REVIEWING;
    }

    public void markFailed() {
        this.status = PolicyLifecycleStatus.FAILED;
    }
}
