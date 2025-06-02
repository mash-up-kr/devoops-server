package com.devoops.dto.response;

import com.devoops.service.auth.jwt.JwtToken;
import java.time.Duration;

public record UserTokenResponse(
        String accessToken,
        String refreshToken,
        Duration refreshTokenExpiration
) {

    public UserTokenResponse(JwtToken accessToken, JwtToken refreshToken, Duration refreshTokenExpiration) {
        this(accessToken.getToken(), refreshToken.getToken(), refreshTokenExpiration);
    }
}
