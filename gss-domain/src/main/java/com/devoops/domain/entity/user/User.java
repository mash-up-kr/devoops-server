package com.devoops.domain.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * pojo domain entity
 */

@Getter
@RequiredArgsConstructor
public class User {

    private final String email;
    private final String token;

    public boolean isSameUser(String email) {
        return this.email.equals(email);
    }
}
