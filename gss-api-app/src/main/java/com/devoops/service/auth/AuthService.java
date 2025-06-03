package com.devoops.service.auth;

import com.devoops.client.GithubOAuthClient;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.AuthResponse;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.jwt.JwtProperties;
import com.devoops.service.auth.jwt.JwtToken;
import com.devoops.service.auth.jwt.JwtTokenManager;
import com.devoops.service.auth.jwt.TokenType;
import com.devoops.dto.request.GithubTokenRequest;
import com.devoops.dto.response.GithubTokenResponse;
import com.devoops.dto.response.UserInfoResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    private final GithubOAuthClient authClient;
    private final JwtTokenManager jwtTokenManager;

    public AuthResponse getUserInfo(UserSaveRequest request) {
        GithubTokenRequest tokenRequest = new GithubTokenRequest(request.code(), request.redirectUrl());
        GithubTokenResponse token = authClient.getToken(tokenRequest);
        UserInfoResponse userInfo = authClient.getUserInfo(token.accessToken());
        return new AuthResponse(token.accessToken(), userInfo.email());
    }

    public UserTokenResponse issueToken(String value) {
        JwtToken accessToken = jwtTokenManager.createToken(value, TokenType.ACCESS_TOKEN);
        JwtToken refreshToken = jwtTokenManager.createToken(value, TokenType.REFRESH_TOKEN);
        Duration refreshTokenExpiration = jwtTokenManager.getTokenExpiration(TokenType.REFRESH_TOKEN);
        return new UserTokenResponse(accessToken, refreshToken, refreshTokenExpiration);
    }

    public UserTokenResponse reissueToken(JwtToken refreshToken) {
        String resolvedValue = jwtTokenManager.resolveToken(refreshToken, TokenType.REFRESH_TOKEN);
        return issueToken(resolvedValue);
    }

    public String resolveToken(JwtToken token, TokenType tokenType) {
        return jwtTokenManager.resolveToken(token, tokenType);
    }
}
