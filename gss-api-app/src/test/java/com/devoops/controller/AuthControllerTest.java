package com.devoops.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseControllerTest;
import com.devoops.domain.entity.auth.RefreshToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.auth.BlackListRepository;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import com.devoops.dto.request.LogoutV1Request;
import com.devoops.dto.request.RefreshTokenV1Request;
import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.dto.response.UserTokenResponse;
import com.devoops.service.auth.TokenManager;
import com.devoops.service.auth.jwt.AccessToken;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.Instant;
import java.util.Date;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class AuthControllerTest extends BaseControllerTest {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private BlackListRepository blackListRepository;

    @Autowired
    private RefreshTokenDomainRepository refreshTokenDomainRepository;

    @Nested
    class Authorize {

        @Test
        void 유효한_엑세스_토큰으로_회원을_인증할_수_있다() {
            User saveUser = userGenerator.generate("김건우");
            AccessToken accessToken = tokenManager.createAccessToken(saveUser.getId());

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .when().get("/test/method")
                    .then().statusCode(HttpStatus.OK.value());
        }

        @Test
        void 블랙_리스트에_등록된_엑세스_토큰으로_회원을_인증할_수_없다() {
            User saveUser = userGenerator.generate("김건우");
            AccessToken accessToken = tokenManager.createAccessToken(saveUser.getId());
            blackListRepository.addBlackList(accessToken.getToken(), Date.from(Instant.now().plusSeconds(1L)));

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .when().get("/test/method")
                    .then().statusCode(HttpStatus.UNAUTHORIZED.value());
        }
    }

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
        void 재발급_이후_리프레시_토큰은_삭제되고_액세스토큰은_블랙리스트에_등록된다_V1() {
            User saveUser = userGenerator.generate("김건우");
            AccessToken accessToken = tokenManager.createAccessToken(saveUser.getId());
            RefreshToken refreshToken = tokenManager.createRefreshToken(saveUser.getId());
            RefreshTokenV1Request request = new RefreshTokenV1Request(accessToken.getToken(), refreshToken.getValue());

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when().post("/api/v1/auth/github/refresh")
                    .then().statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .body()
                    .as(UserTokenResponse.class);

            boolean isRefreshTokenExists = refreshTokenDomainRepository.exists(refreshToken.getValue());
            boolean isBlackListExists = blackListRepository.isExists(accessToken.getToken());
            assertAll(
                    () -> assertThat(isRefreshTokenExists).isFalse(),
                    () -> assertThat(isBlackListExists).isTrue()
            );
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
                    .then().statusCode(HttpStatus.OK.value());
        }

        @Test
        void 로그아웃_한_이후_엑세스_토큰이_블랙리스트에_추가된다_V1() {
            User saveUser = userGenerator.generate("김건우");
            AccessToken accessToken = tokenManager.createAccessToken(saveUser.getId());
            RefreshToken refreshToken = tokenManager.createRefreshToken(saveUser.getId());
            LogoutV1Request logoutRequest = new LogoutV1Request(accessToken.getToken(), refreshToken.getValue());

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .body(logoutRequest)
                    .when().post("/api/v1/auth/logout")
                    .then().statusCode(HttpStatus.OK.value());

            boolean isBlackListExists = blackListRepository.isExists(accessToken.getToken());
            assertThat(isBlackListExists).isTrue();
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
