package com.learn.chatai.infra.persistence;

import com.learn.chatai.domain.job.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

interface JobJpaRepository extends JpaRepository<Job, String> {
}
