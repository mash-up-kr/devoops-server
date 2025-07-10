package com.devoops.dto.request;

public record LogoutV1Request(
        String accessToken,
        String refreshToken
) {

}
