package com.devoops.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoops.BaseControllerTest;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.jwt.AccessToken;
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
            UserSaveRequest request = new UserSaveRequest("testAccessToken");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/api/auth/github")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract().as(UserSaveResponse.class);
        }

        @Test
        void 같은_회원을_대상으로_토큰을_생성_및_발급을_할_수_있다() {
            UserSaveRequest request = new UserSaveRequest("testAccessToken");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/api/auth/github")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract().as(UserSaveResponse.class);

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
            User saveUser = userGenerator.generate("김건우");
            JwtToken refreshToken = jwtTokenManager.createToken(String.valueOf(saveUser.getId()),
                    TokenType.REFRESH_TOKEN);

            UserTokenResponse userTokenResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getToken())
                    .when().post("/api/auth/github/refresh")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .body()
                    .as(UserTokenResponse.class);

            AccessToken accessToken = new AccessToken("Bearer " + userTokenResponse.accessToken());
            String resolvedToken = jwtTokenManager.resolveToken(accessToken);
            assertThat(resolvedToken).isEqualTo(String.valueOf(saveUser.getId()));
        }
    }

    @Nested
    class Logout {

        @Test
        void 로그아웃_할_수_있다() {
            User saveUser = userGenerator.generate("김건우");
            JwtToken accessToken = jwtTokenManager.createToken(String.valueOf(saveUser.getId()),
                    TokenType.ACCESS_TOKEN);
            JwtToken refreshToken = jwtTokenManager.createToken(String.valueOf(saveUser.getId()),
                    TokenType.REFRESH_TOKEN);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getToken())
                    .when().post("/api/auth/logout")
                    .then().statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 로그아웃_시도대상과_리프레시_토큰값이_불일치하면_에러가_발생한다() {
            User saveUser1 = userGenerator.generate("김건우1");
            User saveUser2 = userGenerator.generate("김건우2");
            JwtToken accessToken = jwtTokenManager.createToken(String.valueOf(saveUser1.getId()),
                    TokenType.ACCESS_TOKEN);
            JwtToken refreshToken = jwtTokenManager.createToken(String.valueOf(saveUser2.getId()),
                    TokenType.REFRESH_TOKEN);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getToken())
                    .when().post("/api/auth/logout")
                    .then().statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); //TODO 추후 401로 변경
        }

        @Test
        void 토큰이_없으면_400에러가_발생한다() {
            User saveUser = userGenerator.generate("김건우1");
            JwtToken accessToken = jwtTokenManager.createToken(String.valueOf(saveUser.getId()), TokenType.ACCESS_TOKEN);

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .when().post("/api/auth/logout")
                    .then()
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); //TODO 추후 400으로 변경
        }
    }
}
