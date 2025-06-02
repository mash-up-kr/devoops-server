package com.devoops.service.auth.jwt;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {

    private final JwtProperties jwtProperties;

    public JwtToken createToken(String value, TokenType type) {
        Duration expiration = jwtProperties.getExpirationByTokenType(type);
        return JwtToken.from(value, expiration, type, jwtProperties.getSecretKey());
    }

    public Duration getTokenExpiration(TokenType type) {
        return jwtProperties.getExpirationByTokenType(type);
    }

    public String resolveToken(JwtToken token, TokenType type) {
        return token.resolveToken(jwtProperties.getSecretKey(), type);
    }
}
