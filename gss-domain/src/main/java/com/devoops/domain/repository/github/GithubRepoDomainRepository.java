package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.GithubRepository;

public interface GithubRepoDomainRepository {

    GithubRepository save(GithubRepository githubRepository);

    boolean existsByIdAndUserId(long id, long userId);

    GithubRepository findByIdAndUserId(long id, long userId);
}
