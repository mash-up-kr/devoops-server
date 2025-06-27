package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.exception.GssRepositoryException;
import com.devoops.exception.RepositoryErrorCode;
import com.devoops.jpa.entity.github.GithubRepositoryEntity;
import com.devoops.jpa.entity.user.UserEntity;
import com.devoops.jpa.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class GithubRepoDomainRepositoryImpl implements GithubRepoDomainRepository {

    private final UserJpaRepository userJpaRepository;
    private final GithubRepoJpaRepository repoJpaRepository;

    @Override
    @Transactional
    public GithubRepository save(GithubRepository githubRepository) {
        UserEntity userEntity = userJpaRepository.findById(githubRepository.getUserId()).get();
        GithubRepositoryEntity repositoryEntity = GithubRepositoryEntity.from(githubRepository, userEntity);
        GithubRepositoryEntity savedRepositoryEntity = repoJpaRepository.save(repositoryEntity);
        return savedRepositoryEntity.toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIdAndUserId(long id, long userId) {
        return repoJpaRepository.existsByIdAndUser_Id(id, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public GithubRepository findByIdAndUserId(long id, long userId) {
        GithubRepositoryEntity repositoryEntity = repoJpaRepository.findByIdAndUser_Id(id, userId)
            .orElseThrow(() -> new GssRepositoryException(RepositoryErrorCode.GITHUB_REPOSITORY_NOT_FOUND));

        return repositoryEntity.toDomainEntity();
    }
}
