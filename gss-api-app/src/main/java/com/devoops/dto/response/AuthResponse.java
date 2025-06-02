package com.devoops.dto.response;

import com.devoops.domain.entity.auth.GithubToken;
import com.devoops.domain.entity.auth.UserInfo;

public record AuthResponse(
        String githubToken,
        String email
) {

    public AuthResponse(GithubToken githubToken, UserInfo userInfo) {
        this(githubToken.getToken(), userInfo.email());
    }
}
