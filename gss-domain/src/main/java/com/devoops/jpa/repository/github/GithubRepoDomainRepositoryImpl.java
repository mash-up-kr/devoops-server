package com.devoops.jpa.repository.github;

import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GithubRepoDomainRepositoryImpl implements GithubRepoDomainRepository {

    private final GithubRepoJpaRepository repoJpaRepository;

    @Override
    public boolean existsByIdAndUserId(long id, long userId) {
        return repoJpaRepository.existsByIdAndUserId(id, userId);
    }
}
