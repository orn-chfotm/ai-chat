package com.learn.chatai.infra.persistence;

import com.learn.chatai.domain.policy.entity.PolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;

interface PolicyVersionJpaRepository extends JpaRepository<PolicyVersion, String> {
}
