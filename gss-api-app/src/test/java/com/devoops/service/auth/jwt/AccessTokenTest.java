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

class AccessTokenTest {

    @Nested
    class Create {

        @Test
        void 엑세스_토큰을_만들_수_있다() {
            String value = "test";
            Duration expiration = Duration.ofSeconds(1L);
            SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

            assertThatCode(() -> new AccessToken(value, expiration, secretKey))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class Resolve {

        @Test
        void 엑세스_토큰을_추출할_수_있다() {
            String value = "test";
            TokenType type = TokenType.ACCESS_TOKEN;
            Duration expiration = Duration.ofSeconds(1L);
            SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            AccessToken token =  new AccessToken(value, expiration, secretKey);

            String resolvedToken = token.resolveToken(secretKey);

            assertThat(resolvedToken).isEqualTo(value);
        }

        @Test
        void 만료된_엑세스_토큰에_추출을_시도하면_에러가_발생한다() {
            String value = "test";
            Duration expiration = Duration.ZERO;
            TokenType type = TokenType.ACCESS_TOKEN;
            SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
            AccessToken expiredToken =  new AccessToken(value, expiration, secretKey);

            assertThatThrownBy(() -> expiredToken.resolveToken(secretKey))
                    .isInstanceOf(ExpiredJwtException.class);
        }
    }
}
