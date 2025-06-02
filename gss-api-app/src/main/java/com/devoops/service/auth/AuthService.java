package com.devoops.service.auth;

import com.devoops.domain.entity.auth.GithubToken;
import com.devoops.domain.entity.auth.UserInfo;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.service.auth.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    private final GithubClient githubClient;

    public UserInfo getMemberInfo(UserSaveRequest request) {
        GithubToken token = githubClient.getToken(request.code(), request.redirectUrl());
        return githubClient.getMemberInfo(token);
    }
}
