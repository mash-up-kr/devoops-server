package com.devoops.domain.entity.github;

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

    public GithubRepository(long userId, String name, String url, String owner, int prCount, long externalId) {
        this(null, userId, name, url, owner, prCount, externalId);
    }
}
