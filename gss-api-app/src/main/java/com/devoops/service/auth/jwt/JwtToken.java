package com.devoops.service.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class JwtToken {

    private static final String TOKEN_TYPE_CLAIMS_NAME = "type";

    public static JwtToken from(
            String value,
            Duration expiration,
            TokenType type,
            SecretKey secretKey
    ) {
        return new AccessToken(value, expiration, secretKey);
    }

    protected String makeJwtTokenValue(Date expiredDate, String value, TokenType type, SecretKey secretKey) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(value)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .claim(TOKEN_TYPE_CLAIMS_NAME, type.name())
                .signWith(secretKey)
                .compact();
    }

    protected String resolveJwtToken(SecretKey secretKey, TokenType type, String token) throws JwtException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        validateTokenType(claims, type);
        return claims.getSubject();
    }

    private void validateTokenType(Claims claims, TokenType type) {
        String extractTokenType = claims.get(TOKEN_TYPE_CLAIMS_NAME, String.class);
        if (!extractTokenType.equals(type.name())) {
            throw new RuntimeException(type.name() + " token type is invalid");
        }
    }

    public abstract String resolveToken(SecretKey secretKey);

    public abstract String getToken();
}
