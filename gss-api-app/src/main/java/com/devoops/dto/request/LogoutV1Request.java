package com.devoops.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LogoutV1Request(
        @NotBlank
        @Schema(description = "로그아웃 시도 회원 엑세스 토큰", example = "accessToken")
        String accessToken,

        @NotBlank
        @Schema(description = "로그아웃 시도 회원 리프레시 토큰", example = "oldRefreshToken")
        String refreshToken
) {

}
