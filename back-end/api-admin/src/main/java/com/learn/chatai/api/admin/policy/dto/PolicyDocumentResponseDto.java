package com.learn.chatai.api.admin.policy.dto;

import com.learn.chatai.domain.policy.entity.PolicyDocument;
import com.learn.chatai.domain.policy.entity.PolicyTextRevision;
import com.learn.chatai.domain.policy.service.PolicyIngestionService.PolicyDocumentDetail;

import java.time.LocalDateTime;

public record PolicyDocumentResponseDto(
        String documentId,
        String policyCode,
        String originalFileName,
        String status,
        LocalDateTime createdAt,
        RevisionSummary latestRevision
) {

    public static PolicyDocumentResponseDto from(PolicyDocumentDetail detail) {
        PolicyDocument document = detail.document();
        return new PolicyDocumentResponseDto(
                document.getDocumentId(),
                document.getPolicyCode(),
                document.getOriginalFileName(),
                document.getStatus().name(),
                document.getCreatedAt(),
                detail.latestRevision() == null ? null : RevisionSummary.of(detail.latestRevision())
        );
    }

    public record RevisionSummary(
            String revisionId,
            String revisionType,
            String content,
            Integer extractedPageCount,
            Integer extractedCharacterCount,
            LocalDateTime createdAt
    ) {

        static RevisionSummary of(PolicyTextRevision revision) {
            return new RevisionSummary(
                    revision.getRevisionId(),
                    revision.getRevisionType().name(),
                    revision.getContent(),
                    revision.getExtractedPageCount(),
                    revision.getExtractedCharacterCount(),
                    revision.getCreatedAt()
            );
        }
    }
}
