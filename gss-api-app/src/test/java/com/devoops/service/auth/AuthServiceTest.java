package com.devoops.service.auth;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.auth.RefreshToken;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.jwt.AccessToken;
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

            UserTokenResponse issuedTokens = authService.issueToken(userId);

            String resolvedAccessToken = resolveAccessTokenValue(issuedTokens.accessToken());
            assertThat(resolvedAccessToken).isEqualTo(String.valueOf(userId));
        }

        @Test
        void 토큰을_재발급할_수_있다() {
            long userId = 1L;
            AccessToken token = tokenManager.createAccessToken(userId);
            RefreshToken refreshToken = tokenManager.createRefreshToken(userId);

            UserTokenResponse reissuedTokens = authService.reissueToken(refreshToken.getValue());

            String resolvedAccessToken = resolveAccessTokenValue(reissuedTokens.accessToken());
            assertThat(resolvedAccessToken).isEqualTo(String.valueOf(userId));
        }

        private String resolveAccessTokenValue(String tokenValue) {
            return tokenManager.resolveToken(new AccessToken("Bearer " + tokenValue));
        }
    }
}
