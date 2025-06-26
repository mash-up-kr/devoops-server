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
    private final long externalId;
}
