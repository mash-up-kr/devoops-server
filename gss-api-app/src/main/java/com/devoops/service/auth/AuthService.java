package com.devoops.service.auth;

import com.devoops.client.GithubOAuthClient;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import com.devoops.dto.response.AuthResponse;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.jwt.JwtProperties;
import com.devoops.service.auth.jwt.JwtToken;
import com.devoops.service.auth.jwt.RefreshToken;
import com.devoops.service.auth.jwt.TokenType;
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
    private final TokenManager jwtTokenManager;
    private final RefreshTokenDomainRepository refreshTokenDomainRepository;

    public AuthResponse getUserInfo(String token) {
        UserInfoResponse userInfo = authClient.getUserInfo(token);
        return new AuthResponse(token, userInfo.id(), userInfo.name(), userInfo.avatarUrl());
    }

    public UserTokenResponse issueToken(String value) {
        JwtToken accessToken = jwtTokenManager.createToken(value, TokenType.ACCESS_TOKEN);
        JwtToken refreshToken = jwtTokenManager.createToken(value, TokenType.REFRESH_TOKEN);
        Duration refreshTokenExpiration = jwtTokenManager.getTokenExpiration(TokenType.REFRESH_TOKEN);
        return new UserTokenResponse(accessToken, refreshToken, refreshTokenExpiration);
    }

    public UserTokenResponse reissueToken(RefreshToken refreshToken) {
        String resolvedValue = jwtTokenManager.resolveToken(refreshToken);
        return issueToken(resolvedValue);
    }

    public String resolveToken(JwtToken token) {
        return jwtTokenManager.resolveToken(token);
    }

    public void logout(User user, long id) {
        if (!user.isSameUser(id)) {
            throw new RuntimeException("401 인증 에러");
        }
    }
}
