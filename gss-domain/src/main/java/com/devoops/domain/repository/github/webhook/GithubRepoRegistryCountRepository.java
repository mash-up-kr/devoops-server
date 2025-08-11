package com.devoops.domain.repository.github.webhook;

import com.devoops.domain.entity.github.webhook.GithubRepoRegistryCount;

public interface GithubRepoRegistryCountRepository {

    GithubRepoRegistryCount save(GithubRepoRegistryCount githubRepoRegistryCount);

    GithubRepoRegistryCount getByExternalId(long externalId);

    boolean existsByExternalId(long externalId);

    GithubRepoRegistryCount plusCount(long externalId);

    GithubRepoRegistryCount minusCount(long externalId);

    void deleteByExternalId(long repoExternalId);
}
