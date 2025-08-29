package com.devoops.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.pr.ProcessingStatus;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.pr.PullRequestDomainRepository;
import com.devoops.dto.request.AnswerPutRequest;
import com.devoops.dto.request.AnswerPutRequests;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class QuestionFacadeServiceTest extends BaseServiceTest {

    @Autowired
    private QuestionFacadeService questionFacadeService;

    @Autowired
    private PullRequestDomainRepository pullRequestDomainRepository;

    @Nested
    class StatusChange {

        @Test
        void 최소_하나의_회고가_생성되면_PR상태가_progress로_변경된다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR",
                    RecordStatus.PENDING,
                    ProcessingStatus.DONE,
                    repo,
                    LocalDateTime.now()
            );
            Question question1 = questionGenerator.generate(pullRequest, "질문1");

            questionFacadeService.initializeAnswer(question1.getId(), user);

            PullRequest progressPullRequest = pullRequestDomainRepository.findById(pullRequest.getId());
            assertThat(progressPullRequest.getRecordStatus()).isEqualTo(RecordStatus.PROGRESS);
        }

        @Test
        void 회고를_삭제할_때_PR의_회고가_남아있다면_PR은_PROGRESS를_유지한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PROGRESS,
                    ProcessingStatus.DONE,
                    repo,
                    LocalDateTime.now());
            Question question1 = questionGenerator.generate(pullRequest, "질문1");
            Question question2 = questionGenerator.generate(pullRequest, "질문2");
            Answer answer1 = answerGenerator.generate(question1, "대답1");
            Answer answer2 = answerGenerator.generate(question2, "대답2");

            questionFacadeService.deleteAnswer(answer1.getId());

            PullRequest progressPullRequest = pullRequestDomainRepository.findById(pullRequest.getId());
            assertThat(progressPullRequest.getRecordStatus()).isEqualTo(RecordStatus.PROGRESS);
        }

        @Test
        void 마지막_회고를_삭제하면_PR은_PENDING으로_변경된다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PROGRESS,
                    ProcessingStatus.DONE,
                    repo,
                    LocalDateTime.now());
            Question question1 = questionGenerator.generate(pullRequest, "질문1");
            Answer answer1 = answerGenerator.generate(question1, "대답1");

            questionFacadeService.deleteAnswer(answer1.getId());

            PullRequest progressPullRequest = pullRequestDomainRepository.findById(pullRequest.getId());
            assertThat(progressPullRequest.getRecordStatus()).isEqualTo(RecordStatus.PENDING);
        }
    }

    @Nested
    class UpdateAllAnswers {

        @Test
        void 다수_회고를_업데이트한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pr1 = pullRequestGenerator.generate("PR1", RecordStatus.PENDING, ProcessingStatus.DONE, repo, LocalDateTime.now());
            Question question1 = questionGenerator.generate(pr1, "질문1");
            Question question2 = questionGenerator.generate(pr1, "질문2");
            Answer answer1 = answerGenerator.generate(question1, "answer1");
            Answer answer2 = answerGenerator.generate(question2, "answer2");

            AnswerPutRequests putRequests = new AnswerPutRequests(
                    List.of(
                            new AnswerPutRequest(answer1.getId(), "new answer1"),
                            new AnswerPutRequest(answer2.getId(), "new answer2")
                    )
            );

            List<Answer> responses = questionFacadeService.updateAllAnswers(putRequests).getValues();
            assertAll(
                    () -> assertThat(responses.get(0).getContent()).isEqualTo("new answer1"),
                    () -> assertThat(responses.get(1).getContent()).isEqualTo("new answer2")
            );
        }
    }
}
