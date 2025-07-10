package com.devoops.service.auth;

import com.devoops.client.GithubOAuthClient;
import com.devoops.domain.entity.auth.RefreshToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.auth.BlackListRepository;
import com.devoops.dto.response.AuthResponse;
import com.devoops.dto.response.UserInfoResponse;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.service.auth.jwt.AccessToken;
import com.devoops.service.auth.jwt.JwtProperties;
import com.devoops.service.auth.jwt.TokenType;
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
    private final BlackListRepository blackListRepository;

    public AuthResponse getUserInfo(String token) {
        UserInfoResponse userInfo = authClient.getUserInfo(token);
        return new AuthResponse(token, userInfo.id(), userInfo.name(), userInfo.avatarUrl());
    }

    public UserTokenResponse issueToken(long userId) {
        AccessToken accessToken = jwtTokenManager.createAccessToken(userId);
        RefreshToken refreshToken = jwtTokenManager.createRefreshToken(userId);
        Duration refreshTokenExpiration = jwtTokenManager.getTokenExpiration(TokenType.REFRESH_TOKEN);
        return new UserTokenResponse(accessToken.getToken(), refreshToken.getValue(), refreshTokenExpiration);
    }

    public UserTokenResponse reissueToken(String refreshTokenValue) {
        RefreshToken refreshToken = jwtTokenManager.refresh(refreshTokenValue);
        AccessToken accessToken = jwtTokenManager.createAccessToken(refreshToken.getUserId());
        Duration refreshTokenExpiration = jwtTokenManager.getTokenExpiration(TokenType.REFRESH_TOKEN);
        return new UserTokenResponse(accessToken.getToken(), refreshToken.getValue(), refreshTokenExpiration);
    }

    public String resolveToken(AccessToken token) {
        return jwtTokenManager.resolveToken(token);
    }

    public void logout(String refreshToken, User user) {
        RefreshToken token = jwtTokenManager.getRefreshToken(refreshToken);
        if (!user.isSameUser(token.getUserId())) {
            throw new GssException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        jwtTokenManager.deleteRefreshToken(refreshToken);
    }

    public void logoutV1(String accessToken, String refreshToken, User user) {
        RefreshToken token = jwtTokenManager.getRefreshToken(refreshToken);
        if (!user.isSameUser(token.getUserId())) {
            throw new GssException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        jwtTokenManager.deleteRefreshToken(refreshToken);
        jwtTokenManager.addBlackList(new AccessToken(accessToken));
    }
}
