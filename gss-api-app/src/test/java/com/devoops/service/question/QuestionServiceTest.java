package com.devoops.service.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.entity.user.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class QuestionServiceTest extends BaseServiceTest {

    @Autowired
    private QuestionService questionService;

    @Nested
    class InitializeAnswer {

        @Test
        void 질문에_대한_최초_응답을_저장한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PENDING, repo, LocalDateTime.now());
            Question question = questionGenerator.generate(pullRequest, "질문 : 이거 왜 이렇게 했어요?");

            Answer answer = questionService.initializeAnswer(question.getId(), user);

            assertAll(
                    () -> assertThat(answer.getQuestionId()).isEqualTo(question.getId()),
                    () -> assertThat(answer.getContent()).isEqualTo("")
            );
        }
    }

    @Nested
    class UpdateAnswer {

        @Test
        void 질문에_대한_응답을_업데이트한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PENDING, repo, LocalDateTime.now());
            Question question = questionGenerator.generate(pullRequest, "질문 : 이거 왜 이렇게 했어요?");
            Answer answer = answerGenerator.generate(question, "before");

            Answer updatedAnswer = questionService.updateAnswer(answer.getId(), "after");

            assertAll(
                    () -> assertThat(updatedAnswer.getQuestionId()).isEqualTo(question.getId()),
                    () -> assertThat(updatedAnswer.getContent()).isEqualTo("after")
            );
        }
    }

}
