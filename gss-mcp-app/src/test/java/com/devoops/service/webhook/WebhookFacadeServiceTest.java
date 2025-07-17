package com.devoops.service.webhook;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoops.BaseMcpTest;
import com.devoops.adaptor.GithubAdaptor;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.dto.request.GitHubWebhookEventRequest;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@Slf4j
class WebhookFacadeServiceTest extends BaseMcpTest {

    @Autowired
    private WebhookFacadeService webhookFacadeService;

    @MockitoBean
    private GithubAdaptor adaptor;

    @Autowired
    private QuestionDomainRepository questionDomainRepository;

    @Autowired
    private PullRequestDomainRepository pullRequestDomainRepository;

    @Nested
    class WebHookEventTest {

        @Test
        void 웹_훅_이벤트_발생_시_질문을_생성한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            log.info("repo: {}", repo.getExternalId());
            GitHubWebhookEventRequest request = createClosedMergedPullRequest(user.getProviderId(),
                    repo.getExternalId());
            AppWebhookEventRequest appRequest = createWebhookEventRequest(request);

            webhookFacadeService.createQuestionWithWebhookEvent(appRequest);

            PullRequests pullRequests = pullRequestDomainRepository.findUserPullRequestsOrderByMergedAt(user.getId(), 2,
                    0);
            long savedPrId = pullRequests.getValues().get(0).getId();
            List<QuestionAnswer> questions = questionDomainRepository.findAllPrQuestions(savedPrId);
            assertThat(questions).hasSize(4);
        }
    }

    private AppWebhookEventRequest createWebhookEventRequest(GitHubWebhookEventRequest request) {
        return new AppWebhookEventRequest(
                request.isMerged(),
                request.getExternalId(),
                request.getPullRequestUrl(),
                request.getPullRequestDiffUrl(),
                request.getTitle(),
                request.getDescription(),
                request.getTag(),
                request.getRepositoryId(),
                request.getUserId(),
                request.getMergedAt()
        );
    }

    public static GitHubWebhookEventRequest createClosedMergedPullRequest(long userProviderId, long repoId) {
        GitHubWebhookEventRequest.User mockUser = new GitHubWebhookEventRequest.User(
                "mock-user", // login
                userProviderId
        );

        GitHubWebhookEventRequest.Label label = new GitHubWebhookEventRequest.Label(
                1L,
                "feature"
        );

        GitHubWebhookEventRequest.Repository repository = new GitHubWebhookEventRequest.Repository(
                repoId,
                "mock-repo",
                false, // isPrivate
                mockUser
        );

        GitHubWebhookEventRequest.PullRequest pullRequest = new GitHubWebhookEventRequest.PullRequest(
                "https://api.github.com/repos/mock-org/mock-repo/pulls/42", // url
                987654321L, // id
                "https://github.com/mock-org/mock-repo/pull/42.diff", // diffUrl
                "closed", // state
                "feat: mock 기능 구현", // title
                "이 PR은 mock 기능을 구현합니다.", // body
                List.of(label),
                mockUser,
                true, // merged
                LocalDateTime.of(2025, 7, 14, 12, 0) // mergedAt
        );

        return new GitHubWebhookEventRequest(
                "closed",
                42,
                pullRequest,
                repository
        );
    }
}
