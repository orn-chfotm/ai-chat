package com.learn.chatai.infra.persistence;

import com.learn.chatai.domain.job.entity.Job;
import com.learn.chatai.domain.job.port.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class JobRepositoryAdapter implements JobRepository {

    private final JobJpaRepository jpaRepository;

    @Override
    public Job save(Job job) {
        return jpaRepository.save(job);
    }

    @Override
    public Optional<Job> findById(String jobId) {
        return jpaRepository.findById(jobId);
    }
}
