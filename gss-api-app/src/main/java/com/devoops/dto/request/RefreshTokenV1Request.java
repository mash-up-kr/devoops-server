package com.devoops.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenV1Request(
        @NotBlank
        @Schema(description = "재발급 대상 엑세스 토큰", example = "oldAccessToken")
        String accessToken,

        @NotBlank
        @Schema(description = "재발급 대상 리프레시 토큰", example = "oldRefreshToken")
        String refreshToken
) {

}
