package com.devoops.dto.response;

public record AuthResponse(
        String githubToken,
        long providerId,
        String nickname,
        String profileImageUrl
) {

}
