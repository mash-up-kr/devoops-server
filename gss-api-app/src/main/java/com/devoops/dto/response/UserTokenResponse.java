package com.devoops.dto.response;

import java.time.Duration;

public record UserTokenResponse(
        String accessToken,
        String refreshToken,
        Duration refreshTokenExpiration
) {

}
