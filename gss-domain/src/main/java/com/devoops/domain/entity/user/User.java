package com.devoops.domain.entity.user;

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

    public User(long providerId, String nickname, String profileImageUrl) {
        this(null, providerId, nickname, profileImageUrl);
    }

    public boolean isSameUser(long id) {
        return this.id == id;
    }
}
