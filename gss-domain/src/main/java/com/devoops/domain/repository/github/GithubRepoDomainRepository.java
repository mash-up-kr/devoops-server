package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.GithubRepository;

import java.util.List;

public interface GithubRepoDomainRepository {

    GithubRepository save(GithubRepository githubRepository);

    GithubRepository update(GithubRepository githubRepository);

    boolean existsByIdAndUserId(long id, long userId);

    GithubRepository findByIdAndUserId(long id, long userId);

    List<GithubRepository> findByUserId(long userId);

    boolean existsByExternalIdAndUserId(long externalId, long userId);

    GithubRepository findByExternalId(long externalId);

    void deleteById(long id);
}
