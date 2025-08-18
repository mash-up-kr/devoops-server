package com.devoops.domain.entity.github.repo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubRepository {

    private final Long id;
    private final long userId;
    private final String name;
    private final String url;
    private final String owner;
    private final int prCount;
    private final long externalId;
    private final boolean isTracking;

    public GithubRepository(long userId, String name, String url, String owner, int prCount, long externalId, boolean isTracking) {
        this(null, userId, name, url, owner, prCount, externalId, isTracking);
    }

    public GithubRepository stopTracking() {
        return new GithubRepository(id, userId, name, url, owner, prCount, externalId, false);
    }
}
