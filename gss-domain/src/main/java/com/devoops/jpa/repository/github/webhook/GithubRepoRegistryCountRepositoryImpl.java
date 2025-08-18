package com.devoops.jpa.repository.github.webhook;

import com.devoops.domain.entity.github.webhook.GithubRepoRegistryCount;
import com.devoops.domain.repository.github.webhook.GithubRepoRegistryCountRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.webhook.GithubRepoRegistryCountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class GithubRepoRegistryCountRepositoryImpl implements GithubRepoRegistryCountRepository {

    private final GithubRepoRegistryCountJpaRepository githubRepoRegistryCountJpaRepository;

    @Override
    public GithubRepoRegistryCount save(GithubRepoRegistryCount githubRepoRegistryCount) {
        GithubRepoRegistryCountEntity registryCountEntity = GithubRepoRegistryCountEntity.from(githubRepoRegistryCount);
        return githubRepoRegistryCountJpaRepository.save(registryCountEntity)
                .toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public GithubRepoRegistryCount getByExternalId(long externalId) {
        return findRegistryEntityByExternalId(externalId)
                .toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByExternalId(long externalId) {
        return githubRepoRegistryCountJpaRepository.existsByExternalId(externalId);
    }

    @Override
    @Transactional
    public void plusCount(long externalId) {
        int updatedRow = githubRepoRegistryCountJpaRepository.incrementByExternalId(externalId);
        if (updatedRow == 0) {
            throw new GssException(ErrorCode.GITHUB_REPOSITORY_REGISTRY_COUNT_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void minusCount(long externalId) {
        int updatedRow = githubRepoRegistryCountJpaRepository.decrementByExternalIdIfPositive(externalId);
        if (updatedRow == 0) {
            throw new GssException(ErrorCode.GITHUB_REPOSITORY_REGISTRY_COUNT_NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public void deleteByExternalId(long repoExternalId) {
        githubRepoRegistryCountJpaRepository.deleteByExternalId(repoExternalId);
    }

    private GithubRepoRegistryCountEntity findRegistryEntityByExternalId(long externalId) {
        return githubRepoRegistryCountJpaRepository.findByExternalId(externalId)
                .orElseThrow(() -> new GssException(ErrorCode.GITHUB_REPOSITORY_REGISTRY_COUNT_NOT_FOUND));
    }
}
