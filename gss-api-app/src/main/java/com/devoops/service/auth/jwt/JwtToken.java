package com.devoops.service.auth.jwt;

import com.devoops.domain.entity.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JwtToken {

    private static final String TOKEN_TYPE_CLAIMS_NAME = "type";

    private final String token;
    private final TokenType tokenType;

    public static JwtToken from(
            String value,
            Duration expiration,
            TokenType type,
            SecretKey secretKey
    ) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiration.toMillis());
        String token = Jwts.builder()
                .setSubject(value)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .claim(TOKEN_TYPE_CLAIMS_NAME, type.name())
                .signWith(secretKey)
                .compact();
        return new JwtToken(token, type);
    }

    public String resolveToken(SecretKey secretKey, TokenType type) throws JwtException, ExpiredJwtException {
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
}
