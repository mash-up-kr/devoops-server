package com.devoops.dto.request;

public record RefreshTokenV1Request(
        String accessToken,
        String refreshToken
) {

}
