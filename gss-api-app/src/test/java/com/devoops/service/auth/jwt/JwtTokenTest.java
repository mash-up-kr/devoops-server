package com.devoops.service.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.time.Duration;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JwtTokenTest {

    @Nested
    class Create {

        @Test
        void jwt토큰을_만들_수_있다() {
            String value = "test";
            TokenType type = TokenType.ACCESS_TOKEN;
            Duration expiration = Duration.ofSeconds(1L);
            SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

            assertThatCode(() -> JwtToken.from(value, expiration, type, secretKey))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class Resolve {

        @Test
        void jwt토큰을_추출할_수_있다() {
            String value = "test";
            TokenType type = TokenType.ACCESS_TOKEN;
            Duration expiration = Duration.ofSeconds(1L);
            SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            JwtToken token = JwtToken.from(value, expiration, type, secretKey);

            String resolvedToken = token.resolveToken(secretKey, type);

            assertThat(resolvedToken).isEqualTo(value);
        }

        @Test
        void 다른_타입으로_jwt토큰추출을_시도하면_에러가_발생한다() {
            String value = "test";
            Duration expiration = Duration.ofSeconds(1L);
            SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            JwtToken accessToken = JwtToken.from(value, expiration, TokenType.ACCESS_TOKEN, secretKey);

            assertThatThrownBy(() -> accessToken.resolveToken(secretKey, TokenType.REFRESH_TOKEN))
                    .isInstanceOf(RuntimeException.class); //TODO 커스텀 에러로 전환
        }

        @Test
        void 만료된_jwt토큰에_추출을_시도하면_에러가_발생한다() {
            String value = "test";
            Duration expiration = Duration.ZERO;
            TokenType type = TokenType.ACCESS_TOKEN;
            SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            JwtToken expiredToken = JwtToken.from(value, expiration, type, secretKey);

            assertThatThrownBy(() -> expiredToken.resolveToken(secretKey, type))
                    .isInstanceOf(ExpiredJwtException.class); //TODO 커스텀 에러로 전환
        }
    }

}
