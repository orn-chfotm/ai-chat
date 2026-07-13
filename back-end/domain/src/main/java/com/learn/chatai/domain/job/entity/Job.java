package com.learn.chatai.domain.job.entity;

import com.learn.chatai.domain.job.enums.JobStatus;
import com.learn.chatai.domain.job.enums.JobType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Generic async job ledger row (PRD 08). One table/entity backs every long-running
 * operation (upload, extraction, vectorization, chat completion, prompt update) instead
 * of a per-feature job table, so the {@code GET /api/jobs/{jobId}} endpoint works everywhere.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "job")
public class Job {

    @Id
    @Column(name = "job_id", length = 64)
    private String jobId;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type", nullable = false, length = 50)
    private JobType jobType;

    @Column(name = "target_id", length = 64)
    private String targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private JobStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "queued_at")
    private LocalDateTime queuedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "duration_ms")
    private Long durationMs;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metrics")
    private Map<String, Object> metrics = new HashMap<>();

    private Job(String jobId, JobType jobType, String targetId, String createdBy) {
        this.jobId = jobId;
        this.jobType = jobType;
        this.targetId = targetId;
        this.createdBy = createdBy;
        this.status = JobStatus.QUEUED;
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.queuedAt = now;
    }

    public static Job queued(JobType jobType, String targetId, String createdBy) {
        return new Job("JOB-" + UUID.randomUUID(), jobType, targetId, createdBy);
    }

    public void markRunning() {
        this.status = JobStatus.RUNNING;
        this.startedAt = LocalDateTime.now();
    }

    public void markCompleted() {
        this.status = JobStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
        computeDuration();
    }

    public void markFailed(String reason) {
        this.status = JobStatus.FAILED;
        this.failureReason = reason;
        this.completedAt = LocalDateTime.now();
        computeDuration();
    }

    public void putMetric(String key, Object value) {
        this.metrics.put(key, value);
    }

    private void computeDuration() {
        if (startedAt != null && completedAt != null) {
            this.durationMs = Duration.between(startedAt, completedAt).toMillis();
        }
    }
}
