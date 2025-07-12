package com.devoops.service.facade;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import java.time.LocalDateTime;
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
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PENDING, repo,
                    LocalDateTime.now());
            Question question1 = questionGenerator.generate(pullRequest, "질문1");

            questionFacadeService.initializeAnswer(question1.getId(), user);

            PullRequest progressPullRequest = pullRequestDomainRepository.findById(pullRequest.getId());
            assertThat(progressPullRequest.getRecordStatus()).isEqualTo(RecordStatus.PROGRESS);
        }

        @Test
        void 회고를_삭제할_때_PR의_회고가_남아있다면_PR은_PROGRESS를_유지한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PROGRESS, repo,
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
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PROGRESS, repo,
                    LocalDateTime.now());
            Question question1 = questionGenerator.generate(pullRequest, "질문1");
            Answer answer1 = answerGenerator.generate(question1, "대답1");

            questionFacadeService.deleteAnswer(answer1.getId());

            PullRequest progressPullRequest = pullRequestDomainRepository.findById(pullRequest.getId());
            assertThat(progressPullRequest.getRecordStatus()).isEqualTo(RecordStatus.PENDING);
        }
    }
}
