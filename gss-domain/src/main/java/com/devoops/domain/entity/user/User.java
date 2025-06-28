package com.devoops.domain.entity.user;

import com.devoops.domain.entity.github.GithubToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * pojo domain entity
 */

@Getter
@RequiredArgsConstructor
public class User {

    private final Long id;

    private final long providerId;

    private final String nickname;

    private final String profileImageUrl;

    private final GithubToken githubToken;

    public User(long providerId, String nickname, String profileImageUrl, GithubToken githubToken) {
        this(null, providerId, nickname, profileImageUrl, githubToken);
    }

    public boolean isSameUser(long id) {
        return this.id == id;
    }
}
