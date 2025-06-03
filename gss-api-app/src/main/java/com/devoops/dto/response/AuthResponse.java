package com.devoops.dto.response;

public record AuthResponse(
        String githubToken,
        String email
) {

}
