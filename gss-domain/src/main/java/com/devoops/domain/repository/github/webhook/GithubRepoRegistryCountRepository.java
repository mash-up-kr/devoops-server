package com.devoops.domain.repository.github.webhook;

import com.devoops.domain.entity.github.webhook.GithubRepoRegistryCount;

public interface GithubRepoRegistryCountRepository {

    GithubRepoRegistryCount save(GithubRepoRegistryCount githubRepoRegistryCount);

    GithubRepoRegistryCount getByExternalId(long externalId);

    boolean existsByExternalId(long externalId);

    void plusCount(long externalId);

    void minusCount(long externalId);

    void deleteByExternalId(long repoExternalId);
}
