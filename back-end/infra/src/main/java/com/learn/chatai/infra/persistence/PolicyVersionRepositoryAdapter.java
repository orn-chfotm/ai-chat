package com.learn.chatai.infra.persistence;

import com.learn.chatai.domain.policy.entity.PolicyVersion;
import com.learn.chatai.domain.policy.port.PolicyVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
class PolicyVersionRepositoryAdapter implements PolicyVersionRepository {

    private final PolicyVersionJpaRepository jpaRepository;

    @Override
    public PolicyVersion save(PolicyVersion version) {
        return jpaRepository.save(version);
    }

    @Override
    public Optional<PolicyVersion> findById(String versionId) {
        return jpaRepository.findById(versionId);
    }
}
