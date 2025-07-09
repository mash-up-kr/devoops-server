package com.devoops.service.auth;

import com.devoops.domain.entity.auth.RefreshToken2;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.redis.RefreshTokenDomainRepositoryImpl;
import com.devoops.service.auth.jwt.JwtProperties;
import com.devoops.service.auth.jwt.JwtToken;
import com.devoops.service.auth.jwt.RefreshToken;
import com.devoops.service.auth.jwt.TokenType;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final JwtProperties jwtProperties;
    private final RefreshTokenDomainRepositoryImpl refreshTokenDomainRepository;

    public JwtToken createToken(String value, TokenType type) {
        Duration expiration = jwtProperties.getExpirationByTokenType(type);
        return JwtToken.from(value, expiration, type, jwtProperties.getSecretKey());
    }

    public JwtToken createAccessToken(String value) {
        Duration expiration = jwtProperties.getExpirationByTokenType(TokenType.ACCESS_TOKEN);
        return JwtToken.from(value, expiration, TokenType.ACCESS_TOKEN, jwtProperties.getSecretKey());
    }

    public RefreshToken2 createRefreshToken(long userId) {
        Duration expiration = jwtProperties.getExpirationByTokenType(TokenType.REFRESH_TOKEN);
        RefreshToken2 refreshToken2 = new RefreshToken2(userId, expiration);
        return refreshTokenDomainRepository.save(refreshToken2);
    }

    public RefreshToken2 refresh(String tokenValue) {
        RefreshToken2 refreshToken = getRefreshToken(tokenValue);
        refreshTokenDomainRepository.delete(refreshToken.getUserId());
        RefreshToken2 renewedToken = refreshToken.refresh();
        refreshTokenDomainRepository.save(renewedToken);
        return renewedToken;
    }

    private RefreshToken2 getRefreshToken(String tokenValue) {
        if(!refreshTokenDomainRepository.exists(tokenValue)) {
            throw new GssException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        RefreshToken2 refreshToken = refreshTokenDomainRepository.getRefreshToken(tokenValue);
        if(refreshToken.isExpired()) {
            throw new GssException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return refreshToken;
    }

    public String resolveToken(JwtToken token) {
        return token.resolveToken(jwtProperties.getSecretKey());
    }

    public Duration getTokenExpiration(TokenType type) {
        return jwtProperties.getExpirationByTokenType(type);
    }
}
