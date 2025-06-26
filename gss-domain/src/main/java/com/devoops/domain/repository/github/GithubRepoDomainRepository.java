package com.devoops.domain.repository.github;

public interface GithubRepoDomainRepository {

    boolean existsByIdAndUserId(long id, long userId);
}
