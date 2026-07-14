package com.learn.chatai.api.admin.job.dto;

import com.learn.chatai.domain.job.entity.Job;
import com.learn.chatai.domain.job.enums.JobStatus;
import com.learn.chatai.domain.job.enums.JobType;

import java.time.LocalDateTime;
import java.util.Map;

public record JobStatusResponseDto(
        String jobId,
        JobType jobType,
        String targetId,
        JobStatus status,
        LocalDateTime createdAt,
        LocalDateTime queuedAt,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        Long durationMs,
        String failureReason,
        Map<String, Object> metrics
) {

    public static JobStatusResponseDto from(Job job) {
        return new JobStatusResponseDto(
                job.getJobId(),
                job.getJobType(),
                job.getTargetId(),
                job.getStatus(),
                job.getCreatedAt(),
                job.getQueuedAt(),
                job.getStartedAt(),
                job.getCompletedAt(),
                job.getDurationMs(),
                job.getFailureReason(),
                job.getMetrics()
        );
    }
}
