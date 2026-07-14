package com.learn.chatai.domain.job.port;

import com.learn.chatai.domain.job.entity.Job;

import java.util.Optional;

/**
 * Port implemented by {@code infra.persistence} — domain code never depends on Spring Data directly.
 */
public interface JobRepository {

    Job save(Job job);

    Optional<Job> findById(String jobId);
}
