package com.devoops.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.jwt.JwtToken;
import com.devoops.service.auth.jwt.JwtTokenManager;
import com.devoops.service.auth.jwt.TokenType;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthServiceTest extends BaseServiceTest {

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Autowired
    private AuthService authService;

    //TODO 유효기간을 secretManager에 작성하기
    //TODO 엘라스틱 캐시로 refresh 관리, 블랙리스트 관리

    @Nested
    class IssueTokens {

        @Test
        void 토큰을_발급할_수_있다() {
            String value = UUID.randomUUID().toString();

            UserTokenResponse issuedTokens = authService.issueToken(value);

            String resolvedAccessToken = resolveTokenValue(issuedTokens.accessToken(), TokenType.ACCESS_TOKEN);
            String resolvedRefreshToken = resolveTokenValue(issuedTokens.refreshToken(), TokenType.REFRESH_TOKEN);
            assertAll(
                    () -> assertThat(resolvedAccessToken).isEqualTo(value),
                    () -> assertThat(resolvedRefreshToken).isEqualTo(value)
            );
        }

        @Test
        void 토큰을_재발급할_수_있다() {
            String value = UUID.randomUUID().toString();
            JwtToken refreshToken = jwtTokenManager.createToken(value, TokenType.REFRESH_TOKEN);

            UserTokenResponse reissuedTokens = authService.reissueToken(refreshToken);

            String resolvedAccessToken = resolveTokenValue(reissuedTokens.accessToken(), TokenType.ACCESS_TOKEN);
            String resolvedRefreshToken = resolveTokenValue(reissuedTokens.refreshToken(), TokenType.REFRESH_TOKEN);
            assertAll(
                    () -> assertThat(resolvedAccessToken).isEqualTo(value),
                    () -> assertThat(resolvedRefreshToken).isEqualTo(value)
            );
        }

        private String resolveTokenValue(String tokenValue, TokenType tokenType) {
            JwtToken jwtToken = new JwtToken(tokenValue, tokenType);
            return jwtTokenManager.resolveToken(jwtToken, tokenType);
        }
    }
}
