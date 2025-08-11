package com.devoops.domain.repository.github.repo;

import com.devoops.domain.entity.github.repo.GithubRepoRegistryCount;

public interface GithubRepoRegistryCountRepository {

    GithubRepoRegistryCount save(GithubRepoRegistryCount githubRepoRegistryCount);

    GithubRepoRegistryCount getByExternalId(long externalId);

    boolean existsByExternalId(long externalId);

    GithubRepoRegistryCount plusCount(long externalId);

    GithubRepoRegistryCount minusCount(long externalId);

    void deleteByExternalId(long repoExternalId);
}
