package com.devoops.service.auth;

import com.devoops.domain.entity.auth.RefreshToken;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.service.auth.jwt.AccessToken;
import com.devoops.service.auth.jwt.JwtProperties;
import com.devoops.service.auth.jwt.TokenType;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final JwtProperties jwtProperties;
    private final RefreshTokenDomainRepository refreshTokenDomainRepository;

    public AccessToken createAccessToken(long userId) {
        Duration expiration = jwtProperties.getExpirationByTokenType(TokenType.ACCESS_TOKEN);
        return new AccessToken(String.valueOf(userId), expiration, jwtProperties.getSecretKey());
    }

    public RefreshToken createRefreshToken(long userId) {
        Duration expiration = jwtProperties.getExpirationByTokenType(TokenType.REFRESH_TOKEN);
        RefreshToken refreshToken = new RefreshToken(userId, expiration);
        return refreshTokenDomainRepository.save(refreshToken);
    }

    public RefreshToken refresh(String tokenValue) {
        RefreshToken refreshToken = getRefreshToken(tokenValue);
        refreshTokenDomainRepository.delete(refreshToken.getValue());
        RefreshToken renewedToken = refreshToken.refresh();
        refreshTokenDomainRepository.save(renewedToken);
        return renewedToken;
    }

    public RefreshToken getRefreshToken(String tokenValue) {
        if (!refreshTokenDomainRepository.exists(tokenValue)) {
            throw new GssException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        RefreshToken refreshToken = refreshTokenDomainRepository.getRefreshToken(tokenValue);
        if (refreshToken.isExpired()) {
            throw new GssException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return refreshToken;
    }

    public void deleteRefreshToken(String tokenValue) {
        RefreshToken refreshToken = getRefreshToken(tokenValue);
        refreshTokenDomainRepository.delete(refreshToken.getValue());
    }

    public String resolveToken(AccessToken token) {
        return token.resolveToken(jwtProperties.getSecretKey());
    }

    public Duration getTokenExpiration(TokenType type) {
        return jwtProperties.getExpirationByTokenType(type);
    }
}
