package com.devoops.service.auth.jwt;

import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;

@Getter
public class AccessToken {

    private static final String TOKEN_TYPE_CLAIMS_NAME = "type";
    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    private static final TokenType TOKEN_TYPE = TokenType.ACCESS_TOKEN;

    private final String token;

    public AccessToken(String token) {
        if (!token.startsWith(ACCESS_TOKEN_PREFIX)) {
            throw new GssException(ErrorCode.UNAUTHORIZED_EXCEPTION);
        }
        this.token = token.substring(ACCESS_TOKEN_PREFIX.length()).trim();
    }

    public AccessToken(
            String value,
            Duration expiration,
            SecretKey secretKey
    ) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiration.toMillis());
        String token = makeJwtTokenValue(expiredDate, value, secretKey);
        this.token = token;
    }

    private String makeJwtTokenValue(Date expiredDate, String value, SecretKey secretKey) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(value)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .claim(TOKEN_TYPE_CLAIMS_NAME, TOKEN_TYPE.name())
                .signWith(secretKey)
                .compact();
    }

    public String resolveToken(SecretKey secretKey) {
        return resolveJwtToken(secretKey, token);
    }

    private String resolveJwtToken(SecretKey secretKey, String token) throws JwtException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        validateTokenType(claims, TOKEN_TYPE);
        return claims.getSubject();
    }

    private void validateTokenType(Claims claims, TokenType type) {
        String extractTokenType = claims.get(TOKEN_TYPE_CLAIMS_NAME, String.class);
        if (!extractTokenType.equals(type.name())) {
            throw new GssException(ErrorCode.TOKEN_TYPE_MISMATCH);
        }
    }
}
