package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.jpa.entity.github.GithubRepositoryEntity;
import com.devoops.jpa.entity.user.UserEntity;
import com.devoops.jpa.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.ietf.jgss.GSSException;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GithubRepoDomainRepositoryImpl implements GithubRepoDomainRepository {

    private final UserJpaRepository userJpaRepository;
    private final GithubRepoJpaRepository repoJpaRepository;

    @Override
    public GithubRepository save(GithubRepository githubRepository) {
        UserEntity userEntity = userJpaRepository.findById(githubRepository.getUserId()).get();
        GithubRepositoryEntity repositoryEntity = GithubRepositoryEntity.from(githubRepository, userEntity);
        GithubRepositoryEntity savedRepositoryEntity = repoJpaRepository.save(repositoryEntity);
        return savedRepositoryEntity.toDomainEntity();
    }

    @Override
    public boolean existsByIdAndUserId(long id, long userId) {
        return repoJpaRepository.existsByIdAndUser_Id(id, userId);
    }
}
