package com.devoops.jpa.repository.github.repo;

import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.repository.github.repo.GithubRepoDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.repo.GithubRepositoryEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GithubRepoDomainRepositoryImpl implements GithubRepoDomainRepository {

    private final GithubRepoJpaRepository repoJpaRepository;

    @Override
    @Transactional
    public GithubRepository save(GithubRepository githubRepository) {
        GithubRepositoryEntity repositoryEntity = GithubRepositoryEntity.from(githubRepository);
        GithubRepositoryEntity savedRepositoryEntity = repoJpaRepository.save(repositoryEntity);
        return savedRepositoryEntity.toDomainEntity();
    }

    @Override
    @Transactional
    public GithubRepository update(GithubRepository githubRepository) {
        GithubRepositoryEntity githubRepositoryEntity = repoJpaRepository.findById(githubRepository.getId())
                 .orElseThrow(() -> new GssException(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND));
        return githubRepositoryEntity.update(githubRepository)
                .toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdAndUserId(long id, long userId) {
        return repoJpaRepository.existsByIdAndUserId(id, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByExternalIdAndUserId(long externalId, long userId) {
        return repoJpaRepository.existsByGithubRepositoryIdAndUserId(externalId, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public GithubRepository findByIdAndUserId(long id, long userId) {
        GithubRepositoryEntity repositoryEntity = repoJpaRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new GssException(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND));

        return repositoryEntity.toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GithubRepository> findByUserId(long userId) {
        return repoJpaRepository.findAllByUserId(userId)
            .stream()
            .map(GithubRepositoryEntity::toDomainEntity)
            .toList();
    }

    @Override
    public Optional<GithubRepository> findByExternalIdAndUserId(long externalId, long userId) {
        return repoJpaRepository.findByGithubRepositoryIdAndUserId(externalId, userId)
                .map(GithubRepositoryEntity::toDomainEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public GithubRepository getByExternalIdAndUserId(long externalId, long userId) {
        return repoJpaRepository.findByGithubRepositoryIdAndUserId(externalId, userId)
                .orElseThrow(() -> new GssException(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND))
                .toDomainEntity();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        repoJpaRepository.deleteById(id);
    }
}

