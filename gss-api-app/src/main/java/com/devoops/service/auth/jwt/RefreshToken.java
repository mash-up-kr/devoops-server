package com.devoops.service.auth.jwt;

import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshToken extends JwtToken {

    private static final TokenType TOKEN_TYPE = TokenType.REFRESH_TOKEN;

    private final String token;

    public RefreshToken(
            String value,
            Duration expiration,
            SecretKey secretKey
    ) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + expiration.toMillis());
        String token = makeJwtTokenValue(expiredDate, value, TOKEN_TYPE, secretKey);
        this.token = token;
    }

    @Override
    public String resolveToken(SecretKey secretKey) {
        return resolveJwtToken(secretKey, TOKEN_TYPE, token);
    }
}
