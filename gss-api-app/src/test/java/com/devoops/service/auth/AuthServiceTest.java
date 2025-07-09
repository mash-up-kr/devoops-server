package com.devoops.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.jwt.AccessToken;
import com.devoops.service.auth.jwt.JwtToken;
import com.devoops.service.auth.jwt.RefreshToken;
import com.devoops.service.auth.jwt.TokenType;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthServiceTest extends BaseServiceTest {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private AuthService authService;

    //TODO 엘라스틱 캐시로 refresh 관리, 블랙리스트 관리

    @Nested
    class IssueTokens {

        @Test
        void 토큰을_발급할_수_있다() {
            long userId = 1L;

            UserTokenResponse issuedTokens = authService.issueToken2(userId);

            String resolvedAccessToken = resolveTokenValue(issuedTokens.accessToken(), TokenType.ACCESS_TOKEN);
            assertThat(resolvedAccessToken).isEqualTo(userId);
        }

        @Test
        void 토큰을_재발급할_수_있다() {
            String value = UUID.randomUUID().toString();
            JwtToken token = tokenManager.createToken(value, TokenType.REFRESH_TOKEN);
            RefreshToken refreshToken = new RefreshToken(token.getToken());

            UserTokenResponse reissuedTokens = authService.reissueToken2(refreshToken.getToken());

            String resolvedAccessToken = resolveTokenValue(reissuedTokens.accessToken(), TokenType.ACCESS_TOKEN);
            assertThat(resolvedAccessToken).isEqualTo(value);
        }

        private String resolveTokenValue(String tokenValue, TokenType tokenType) {
            if(tokenType.isAccess()) {
                return tokenManager.resolveToken(new AccessToken("Bearer " + tokenValue));
            }
            return tokenManager.resolveToken(new RefreshToken(tokenValue));
        }
    }
}
