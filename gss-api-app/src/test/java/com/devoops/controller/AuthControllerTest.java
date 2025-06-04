package com.devoops.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoops.BaseControllerTest;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.service.auth.jwt.JwtToken;
import com.devoops.service.auth.jwt.JwtTokenManager;
import com.devoops.service.auth.jwt.TokenType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class AuthControllerTest extends BaseControllerTest {

    @Autowired
    private UserDomainRepository userDomainRepository;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Nested
    class IssueToken {

        @Test
        void 회원_생성_및_토큰_발급을_할_수_있다() {
            UserSaveRequest request = new UserSaveRequest("testCode", "testRedirectUrl");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/api/auth/github")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract().as(UserSaveResponse.class);
        }
    }

    @Nested
    class ReIssueToken {

        @Test
        void 회원의_토큰을_재발급할_수_있다() {
            User saveUser = userDomainRepository.saveUser(new User("email", "token"));
            JwtToken refreshToken = jwtTokenManager.createToken(saveUser.getEmail(), TokenType.REFRESH_TOKEN);

            String reissuedAccessToken = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getToken())
                    .when().post("/api/auth/github/reissue")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract().header(HttpHeaders.AUTHORIZATION);

            JwtToken accessToken = new JwtToken(reissuedAccessToken, TokenType.ACCESS_TOKEN);
            String resolvedToken = jwtTokenManager.resolveToken(accessToken, TokenType.ACCESS_TOKEN);
            assertThat(resolvedToken).isEqualTo(saveUser.getEmail());
        }
    }
}
