package com.devoops.service.auth.jwt;

import com.devoops.domain.entity.user.User;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenManager {

    private final JwtProperties jwtProperties;

    public JwtToken createToken(User user, TokenType type) {
        Duration expiration = jwtProperties.getExpirationByTokenType(type);
        return JwtToken.from(user, expiration, type, jwtProperties.getSecretKey());
    }

    public String resolveToken(JwtToken token, TokenType type) {
        return token.resolveToken(jwtProperties.getSecretKey(), type);
    }
}
