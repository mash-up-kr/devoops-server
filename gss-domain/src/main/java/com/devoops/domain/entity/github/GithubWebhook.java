package com.devoops.domain.entity.github;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubWebhook {

    private final Long id;
    private final long externalId;
    private final long repositoryId;

    public GithubWebhook(long externalId, long repositoryId) {
        this(null, externalId, repositoryId);
    }
}
