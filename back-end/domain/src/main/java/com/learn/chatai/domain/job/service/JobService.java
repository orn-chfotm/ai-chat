package com.learn.chatai.domain.job.service;

import com.learn.chatai.domain.job.entity.Job;
import com.learn.chatai.domain.job.enums.JobType;
import com.learn.chatai.domain.job.exception.JobErrorCode;
import com.learn.chatai.domain.job.exception.JobException;
import com.learn.chatai.domain.job.port.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job create(JobType jobType, String targetId, String createdBy) {
        return jobRepository.save(Job.queued(jobType, targetId, createdBy));
    }

    @Transactional(readOnly = true)
    public Job getOrThrow(String jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new JobException(JobErrorCode.JOB_NOT_FOUND, "Job을 찾을 수 없습니다: " + jobId));
    }

    public Job markRunning(String jobId) {
        Job job = getOrThrow(jobId);
        job.markRunning();
        return jobRepository.save(job);
    }

    public Job markCompleted(String jobId) {
        Job job = getOrThrow(jobId);
        job.markCompleted();
        return jobRepository.save(job);
    }

    public Job markFailed(String jobId, String reason) {
        Job job = getOrThrow(jobId);
        job.markFailed(reason);
        return jobRepository.save(job);
    }

    public void putMetric(String jobId, String key, Object value) {
        Job job = getOrThrow(jobId);
        job.putMetric(key, value);
        jobRepository.save(job);
    }
}
