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
            User saveUser = userDomainRepository.saveUser(new User(1L, "nickname", "profile_url"));
            JwtToken refreshToken = jwtTokenManager.createToken(String.valueOf(saveUser.getProviderId()),
                    TokenType.REFRESH_TOKEN);

            String reissuedAccessToken = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getToken())
                    .when().post("/api/auth/github/reissue")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract().header(HttpHeaders.AUTHORIZATION);

            JwtToken accessToken = new JwtToken(reissuedAccessToken, TokenType.ACCESS_TOKEN);
            String resolvedToken = jwtTokenManager.resolveToken(accessToken, TokenType.ACCESS_TOKEN);
            assertThat(resolvedToken).isEqualTo(saveUser.getProviderId());
        }
    }

    @Nested
    class Logout {

        @Test
        void 로그아웃_할_수_있다() {
            User saveUser = userDomainRepository.saveUser(new User(1L, "nickname", "profile_url"));
            JwtToken accessToken = jwtTokenManager.createToken(String.valueOf(saveUser.getProviderId()),
                    TokenType.ACCESS_TOKEN);
            JwtToken refreshToken = jwtTokenManager.createToken(String.valueOf(saveUser.getProviderId()),
                    TokenType.REFRESH_TOKEN);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getToken())
                    .when().post("/api/auth/logout")
                    .then().statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 로그아웃_시도대상과_리프레시_토큰값이_불일치하면_에러가_발생한다() {
            User saveUser1 = userDomainRepository.saveUser(new User(1L, "nickname1", "profile_url1"));
            User saveUser2 = userDomainRepository.saveUser(new User(2L, "nickname2", "profile_url2"));
            JwtToken accessToken = jwtTokenManager.createToken(String.valueOf(saveUser1.getProviderId()),
                    TokenType.ACCESS_TOKEN);
            JwtToken refreshToken = jwtTokenManager.createToken(String.valueOf(saveUser2.getProviderId()),
                    TokenType.REFRESH_TOKEN);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getToken())
                    .when().post("/api/auth/logout")
                    .then().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); //TODO 추후 401로 변경
        }

        @Test
        void 토큰이_없으면_400에러가_발생한다() {
            User saveUser = userDomainRepository.saveUser(new User(1L, "nickname1", "profile_url1"));
            JwtToken accessToken = jwtTokenManager.createToken(String.valueOf(saveUser.getProviderId()),
                    TokenType.ACCESS_TOKEN);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken.getToken())
                    .when().post("/api/auth/logout")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }
}
