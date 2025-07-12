package com.devoops.dto.response;

import com.devoops.domain.entity.user.User;

public record UserReadResponse(
        long id,
        String nickname,
        String profileImageUrl
) {

    public UserReadResponse(User user) {
        this(
                user.getId(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }
}
