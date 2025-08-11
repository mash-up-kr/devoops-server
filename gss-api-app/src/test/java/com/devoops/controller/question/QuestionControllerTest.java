package com.devoops.controller.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseControllerTest;
import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.AnswerPutRequest;
import com.devoops.dto.request.AnswerPutRequests;
import com.devoops.dto.response.AnswerPutResponses;
import com.devoops.service.auth.jwt.AccessToken;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class QuestionControllerTest extends BaseControllerTest {

    @Nested
    class AnswerPut {

        @Test
        void 다수_회고를_업데이트한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pr1 = pullRequestGenerator.generate("PR1", RecordStatus.PENDING, repo, LocalDateTime.now());
            Question question1 = questionGenerator.generate(pr1, "질문1");
            Question question2 = questionGenerator.generate(pr1, "질문2");
            Answer answer1 = answerGenerator.generate(question1, "answer1");
            Answer answer2 = answerGenerator.generate(question2, "answer2");
            AccessToken accessToken = tokenManager.createAccessToken(user.getId());

            AnswerPutRequests putRequests = new AnswerPutRequests(
                    List.of(
                            new AnswerPutRequest(answer1.getId(), "new answer1"),
                            new AnswerPutRequest(answer2.getId(), "new answer2")
                    )
            );

            AnswerPutResponses responses = RestAssured.given()
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken.getToken())
                    .contentType(ContentType.JSON)
                    .when()
                    .body(putRequests)
                    .put("/api/questions/answer")
                    .then().statusCode(HttpStatus.OK.value())
                    .extract()
                    .as(AnswerPutResponses.class);

            assertAll(
                    () -> assertThat(responses.answers().get(0).content()).isEqualTo("new answer1"),
                    () -> assertThat(responses.answers().get(1).content()).isEqualTo("new answer2")
            );
        }
    }
}
