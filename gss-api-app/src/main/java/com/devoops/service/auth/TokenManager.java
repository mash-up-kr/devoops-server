package com.devoops.service.auth;

import com.devoops.service.auth.jwt.JwtProperties;
import com.devoops.service.auth.jwt.JwtToken;
import com.devoops.service.auth.jwt.TokenType;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenManager {

    private final JwtProperties jwtProperties;

    public JwtToken createToken(String value, TokenType type) {
        Duration expiration = jwtProperties.getExpirationByTokenType(type);
        return JwtToken.from(value, expiration, type, jwtProperties.getSecretKey());
    }

    public Duration getTokenExpiration(TokenType type) {
        return jwtProperties.getExpirationByTokenType(type);
    }

    public String resolveToken(JwtToken token) {
        return token.resolveToken(jwtProperties.getSecretKey());
    }
}
