package com.devoops.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserTokenRefreshResponse(
        @Schema(description = "깃허브 회원 아이디", example = "refershAccessToken")
        String accessToken,

        @Schema(description = "재발급한 리프레시 토큰", example = "refreshRefreshToken")
        String refreshToken
) {

}
