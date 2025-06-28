package com.devoops.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserTokenRefreshResponse(
        @Schema()
        String accessToken,
        String refreshToken
) {

}
