package com.devoops.dto.response;

import com.devoops.domain.entity.user.User;

public record UserReadResponse(
        long id,
        long externalId,
        String nickname,
        String profileImageUrl
) {

    public UserReadResponse(User user) {
        this(
                user.getId(),
                user.getProviderId(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
    }
}
