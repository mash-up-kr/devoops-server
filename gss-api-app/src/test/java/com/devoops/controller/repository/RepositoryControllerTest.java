package com.devoops.controller.repository;

import com.devoops.BaseControllerTest;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.RepositorySaveRequest;
import com.devoops.service.auth.jwt.AccessToken;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class RepositoryControllerTest extends BaseControllerTest {

    @Nested
    class GetRepositoryPullRequests {

        @Test
        void 레포지토리의_풀_리퀘스트_목록을_가져온다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            AccessToken accessToken = tokenManager.createAccessToken(user.getId());
            GithubRepository repo = repoGenerator.generate(user, "김건우의 레포지토리");

            PullRequest pr1 = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING, repo, now.minusMinutes(5L));
            PullRequest pr2 = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING, repo, now.minusMinutes(3L));
            PullRequest pr3 = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING, repo, now.minusMinutes(1L));

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .queryParam("page", 0L)
                    .queryParam("size", 6L)
                    .pathParam("repositoryId", repo.getId())
                    .when().get("/api/repositories/{repositoryId}/pull-requests")
                    .then().statusCode(HttpStatus.OK.value());
        }

        @Test
        void 레포지토리_주인이_아니면_풀리퀘스트_목록을_조회할_수_없다() {
            LocalDateTime now = LocalDateTime.now();
            User beomgeun = userGenerator.generate("범근이 형");
            User seonwoo = userGenerator.generate("선우 누나");
            AccessToken accessToken = tokenManager.createAccessToken(seonwoo.getId());
            GithubRepository repo = repoGenerator.generate(beomgeun, "범근형의 레포지토리");

            PullRequest pr1 = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING, repo, now.minusMinutes(5L));

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .queryParam("page", 0L)
                    .queryParam("size", 6L)
                    .pathParam("repositoryId", repo.getId())
                    .when().get("/api/repositories/{repositoryId}/pull-requests")
                    .then().statusCode(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    class CreateRepository {

        @Test
        void 레포지토리를_생성할_수_있다() {
            User user = userGenerator.generate("김건우");
            AccessToken accessToken = tokenManager.createAccessToken(user.getId());
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");

            RestAssured.given()
                    .contentType(ContentType.JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .body(request)
                    .when().post("/api/repositories")
                    .then().statusCode(HttpStatus.CREATED.value());
        }
    }
}
