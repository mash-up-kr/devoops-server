package com.devoops.domain.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * pojo domain entity
 */

@Getter
@RequiredArgsConstructor
public class User {

    private final long providerId;

    private final String nickname;

    private final String profileImageUrl;

    public boolean isSameUser(long providerId) {
        return this.providerId == providerId;
    }
}
