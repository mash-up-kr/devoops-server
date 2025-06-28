package com.devoops.dto.response;

import com.devoops.domain.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserSaveResponse(

        @Schema(description = "깃허브 회원 아이디", example = "234558")
        long providerId,

        @Schema(description = "깃허브 회원 닉네임", example = "my_nickname")
        String nickname,

        @Schema(description = "깃허브 프로필 url", example = "https://avatars.githubusercontent.com/u/148152234?v=4")
        String profileImageUrl,

        @Schema(description = "엑세스 토큰", example = "accesstokenValue..")
        String accessToken,

        @Schema(description = "리프레시 토큰", example = "refreshTokenValue..")
        String refreshToken
) {

    public UserSaveResponse(User user, String accessToken, String refreshToken) {
        this(user.getProviderId(), user.getNickname(), user.getProfileImageUrl(), accessToken, refreshToken);
    }
}
