package com.devoops.dto.response;

import com.devoops.domain.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nonnull;

public record UserSaveResponse(

        @Nonnull
        @Schema(description = "깃허브 회원 아이디", example = "")
        long providerId,

        @Schema(description = "깃허브 회원 닉네임", example = "my_nickname")
        String nickname,

        @Schema(description = "깃허브 프로필 url", example = "https://avatars.githubusercontent.com/u/148152234?v=4")
        String profileImageUrl
) {

    public UserSaveResponse(User user) {
        this(user.getProviderId(), user.getNickname(), user.getProfileImageUrl());
    }
}
