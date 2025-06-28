package com.devoops.command.request;

import com.devoops.domain.entity.github.GithubRepository;

public record RepositoryCreateCommand(
        long userId,
        String repositoryName,
        String url,
        String ownerName,
        long externalId
) {

    public GithubRepository toDomainEntity() {
        return new GithubRepository(userId, repositoryName, url, ownerName, externalId);
    }
}
