package com.devoops.service.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
import com.devoops.command.request.AnswerUpdateCommand;
import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.jpa.repository.github.answer.AnswerJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class QuestionServiceTest extends BaseServiceTest {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerJpaRepository answerJpaRepository;

    @Nested
    class AnswerCRUD {

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

        @Test
        void 질문에_대한_다수_응답을_업데이트한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PENDING, repo, LocalDateTime.now());
            Question question1 = questionGenerator.generate(pullRequest, "질문1");
            Question question2 = questionGenerator.generate(pullRequest, "질문1");
            Question question3 = questionGenerator.generate(pullRequest, "질문1");
            Answer answer1 = answerGenerator.generate(question1, "before1");
            Answer answer2 = answerGenerator.generate(question2, "before2");
            Answer answer3 = answerGenerator.generate(question3, "before3");
            List<AnswerUpdateCommand> answerUpdateCommands = List.of(
                            new AnswerUpdateCommand(answer1.getId(), "after1"),
                            new AnswerUpdateCommand(answer2.getId(), "after2"),
                            new AnswerUpdateCommand(answer3.getId(), "after3")

            );

            List<Answer> updatedAllAnswers = questionService.updateAllAnswers(answerUpdateCommands).getValues();

            assertAll(
                    () -> assertThat(updatedAllAnswers.get(0).getContent()).isEqualTo("after1"),
                    () -> assertThat(updatedAllAnswers.get(1).getContent()).isEqualTo("after2"),
                    () -> assertThat(updatedAllAnswers.get(2).getContent()).isEqualTo("after3")
            );
        }
    }

    @Nested
    class PRQuestions {

        @Test
        void 풀리퀘스트의_질문과_대답을_모두_조회한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PENDING, repo, LocalDateTime.now());
            Question question1 = questionGenerator.generate(pullRequest, "질문1");
            questionGenerator.generate(pullRequest, "질문1");
            Answer answer1 = answerGenerator.generate(question1, "answerContent");

            List<QuestionAnswer> prQuestions = questionService.getAllPrQuestions(pullRequest.getId());

            assertAll(
                    () -> assertThat(prQuestions).hasSize(2),
                    () -> assertThat(prQuestions.get(0).getAnswer()).isEqualTo(answer1.getContent()),
                    () -> assertThat(prQuestions.get(1).getAnswer()).isNull()
            );
        }
    }
}
