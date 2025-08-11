package com.devoops.service.webhook;

import com.devoops.command.request.PullRequestCreateCommand;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.repo.GithubRepoDomainRepository;
import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.event.QuestionCreateEvent;
import com.devoops.service.pullrequest.PullRequestService;
import com.devoops.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookFacadeService {

    private final GithubRepoDomainRepository githubRepoDomainRepository;
    private final PullRequestService pullRequestService;
    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;


    public void createQuestionWithWebhookEvent(AppWebhookEventRequest request) {
        if (Boolean.FALSE.equals(request.isMerged())) {
            return;
        }
        log.info("request : {}", request);

        User triggerUser = userService.findByProviderId(request.userId());
        GithubToken githubToken = triggerUser.getGithubToken();
        // 레포 아이디를 기반으로 찾기 -> 풀리퀘 생성 -> prCount 올리기
        PullRequest readyPullRequest = savePullRequest(
                triggerUser.getId(),
                request
        );

        eventPublisher.publishEvent(new QuestionCreateEvent(this, request, readyPullRequest, githubToken));
    }

    private PullRequest savePullRequest(
            long userId,
            AppWebhookEventRequest request
    ) {
        GithubRepository githubRepository = githubRepoDomainRepository.findByExternalId(request.repositoryId());
        PullRequestCreateCommand prCreateCommand = resolvePRCreateCommand(request, githubRepository.getId(), userId);
        return pullRequestService.save(prCreateCommand);
    }

    private PullRequestCreateCommand resolvePRCreateCommand(
            AppWebhookEventRequest appWebhookEventRequest,
            long repositoryId,
            long userId
    ) {
        return new PullRequestCreateCommand(
                repositoryId,
                userId,
                appWebhookEventRequest.title(),
                appWebhookEventRequest.description(),
                appWebhookEventRequest.getParsedUrl().getValue(),
                appWebhookEventRequest.pullRequestId(),
                appWebhookEventRequest.label(),
                appWebhookEventRequest.mergedAt()
        );
    }
}
