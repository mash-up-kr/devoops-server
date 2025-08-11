package com.devoops.domain.entity.github.repo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubRepoRegistryCount {

    private final long externalId;
    private final long reposCount;

    public boolean greaterThan(long reposCount) {
        return this.reposCount > reposCount;
    }
}
