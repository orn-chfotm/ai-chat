package com.learn.chatai.domain.policy.port;

import com.learn.chatai.domain.policy.entity.PolicyVersion;

import java.util.Optional;

public interface PolicyVersionRepository {

    PolicyVersion save(PolicyVersion version);

    Optional<PolicyVersion> findById(String versionId);
}
