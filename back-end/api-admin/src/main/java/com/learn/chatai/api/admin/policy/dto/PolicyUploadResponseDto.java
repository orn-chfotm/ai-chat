package com.learn.chatai.api.admin.policy.dto;

import com.learn.chatai.domain.job.entity.Job;
import com.learn.chatai.domain.policy.entity.PolicyDocument;

public record PolicyUploadResponseDto(
        String documentId,
        String jobId,
        String status
) {

    public static PolicyUploadResponseDto of(PolicyDocument document, Job job) {
        return new PolicyUploadResponseDto(document.getDocumentId(), job.getJobId(), document.getStatus().name());
    }
}
