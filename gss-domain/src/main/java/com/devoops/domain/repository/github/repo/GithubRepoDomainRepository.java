package com.devoops.domain.repository.github.repo;

import com.devoops.domain.entity.github.repo.GithubRepository;

import java.util.List;
import java.util.Optional;

public interface GithubRepoDomainRepository {

    GithubRepository save(GithubRepository githubRepository);

    GithubRepository update(GithubRepository githubRepository);

    boolean existsByIdAndUserId(long id, long userId);

    GithubRepository findByIdAndUserId(long id, long userId);

    List<GithubRepository> findByUserId(long userId);

    boolean existsByExternalIdAndUserId(long externalId, long userId);

    Optional<GithubRepository> findByExternalIdAndUserId(long externalId, long userId);

    GithubRepository getByExternalIdAndUserId(long externalId, long userId);

    void deleteById(long id);
}
