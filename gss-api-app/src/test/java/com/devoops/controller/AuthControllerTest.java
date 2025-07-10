package com.devoops.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoops.BaseControllerTest;
import com.devoops.domain.entity.auth.RefreshToken;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.TokenManager;
import com.devoops.service.auth.jwt.AccessToken;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class AuthControllerTest extends BaseControllerTest {

    @Autowired
    private TokenManager tokenManager;

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
            RefreshToken refreshToken = tokenManager.createRefreshToken(saveUser.getId());

            UserTokenResponse userTokenResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getValue())
                    .when().post("/api/auth/github/refresh")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .body()
                    .as(UserTokenResponse.class);

            AccessToken accessToken = new AccessToken(userTokenResponse.accessToken());
            String resolvedToken = tokenManager.resolveToken(accessToken);
            assertThat(resolvedToken).isEqualTo(String.valueOf(saveUser.getId()));
        }

        @Test
        void 재발급한_토큰은_기존의_토큰과_일치하지_않는다() {
            User saveUser = userGenerator.generate("김건우");
            RefreshToken refreshToken = tokenManager.createRefreshToken(saveUser.getId());

            UserTokenResponse userTokenResponse = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getValue())
                    .when().post("/api/auth/github/refresh")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .body()
                    .as(UserTokenResponse.class);

            String refreshedRefreshToken = userTokenResponse.refreshToken();
            assertThat(refreshToken.getValue()).isNotEqualTo(refreshedRefreshToken);
        }
    }

    @Nested
    class Logout {

        @Test
        void 로그아웃_할_수_있다() {
            User saveUser = userGenerator.generate("김건우");
            AccessToken accessToken = tokenManager.createAccessToken(saveUser.getId());
            RefreshToken refreshToken = tokenManager.createRefreshToken(saveUser.getId());

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getValue())
                    .when().post("/api/auth/logout")
                    .then().statusCode(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 로그아웃_시도대상과_리프레시_토큰값이_불일치하면_에러가_발생한다() {
            User saveUser1 = userGenerator.generate("김건우1");
            User saveUser2 = userGenerator.generate("김건우2");
            AccessToken accessToken = tokenManager.createAccessToken(saveUser1.getId());
            RefreshToken refreshToken = tokenManager.createRefreshToken(saveUser2.getId());

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getValue())
                    .when().post("/api/auth/logout")
                    .then().statusCode(HttpStatus.UNAUTHORIZED.value());
        }

        @Test
        void 엑세스_토큰이_없으면_401에러가_발생한다() {
            User saveUser = userGenerator.generate("김건우1");
            RefreshToken refreshToken = tokenManager.createRefreshToken(saveUser.getId());

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.COOKIE, "refreshToken=" + refreshToken.getValue())
                    .when().post("/api/auth/logout")
                    .then()
                    .statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
