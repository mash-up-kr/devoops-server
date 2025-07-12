package com.devoops.service.auth.jwt;

import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import java.time.Duration;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class AccessToken extends JwtToken {

    private static final String ACCESS_TOKEN_PREFIX = "Bearer ";
    private static final TokenType TOKEN_TYPE = TokenType.ACCESS_TOKEN;

    private final String token;


    public AccessToken(String token) {
        if(!token.startsWith(ACCESS_TOKEN_PREFIX)) {
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
        String token = makeJwtTokenValue(expiredDate, value, TOKEN_TYPE, secretKey);
        this.token = token;
    }

    @Override
    public String resolveToken(SecretKey secretKey) {
        return resolveJwtToken(secretKey, TOKEN_TYPE, token);
    }
}
