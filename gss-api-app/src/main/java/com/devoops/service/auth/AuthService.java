package com.devoops.service.auth;

import com.devoops.domain.entity.auth.GithubToken;
import com.devoops.domain.entity.auth.UserInfo;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.jwt.JwtProperties;
import com.devoops.service.auth.jwt.JwtToken;
import com.devoops.service.auth.jwt.JwtTokenManager;
import com.devoops.service.auth.jwt.TokenType;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    private final GithubClient githubClient;
    private final JwtTokenManager jwtTokenManager;

    public UserInfo getUserInfo(UserSaveRequest request) {
        GithubToken token = githubClient.getToken(request.code(), request.redirectUrl());
        return githubClient.getMemberInfo(token);
    }

    public UserTokenResponse issueToken(User user) {
        JwtToken accessToken = jwtTokenManager.createToken(user, TokenType.ACCESS_TOKEN);
        JwtToken refreshToken = jwtTokenManager.createToken(user, TokenType.REFRESH_TOKEN);
        Duration refreshTokenExpiration = jwtTokenManager.getTokenExpiration(TokenType.REFRESH_TOKEN);
        return new UserTokenResponse(accessToken, refreshToken, refreshTokenExpiration);
    }

    public UserTokenResponse reissueToken(JwtToken refreshToken) {
        String userExternalId = jwtTokenManager.resolveToken(refreshToken, TokenType.REFRESH_TOKEN);
        User user = new User(userExternalId);
        return issueToken(user);
    }

    public String resolveToken(JwtToken token, TokenType tokenType) {
        return jwtTokenManager.resolveToken(token, tokenType);
    }
}
