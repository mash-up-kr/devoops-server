package com.devoops.service.auth.jwt;

import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import javax.crypto.SecretKey;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private final String secretKey;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;

    public JwtProperties(String secretKey, Duration accessTokenExpiration, Duration refreshTokenExpiration) {
        this.secretKey = secretKey;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }


    public Duration getExpirationByTokenType(TokenType type) {
        if(type == TokenType.ACCESS_TOKEN) {
            return accessTokenExpiration;
        }
        return refreshTokenExpiration;
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}

