package com.devoops.controller.webhook;

import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.dto.request.GitHubWebhookEventRequest;
import com.devoops.service.webhook.WebhookFacadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookFacadeService webhookFacadeService;

    /**
     * webhook url : https://mcp.dev-oops.kr/api/webhooks/github/pull-requests
     */
    @PostMapping("/github/pull-requests/{repositoryId}")
    public ResponseEntity<Void> getWebhookPullRequest(
            @RequestBody GitHubWebhookEventRequest gitHubWebhookEventRequest,
            @PathVariable(name = "repositoryId") Long appRepositoryId
    ) {
        log.info("request : {} ", gitHubWebhookEventRequest);
        if(gitHubWebhookEventRequest.isMerged()) {
            AppWebhookEventRequest request = createWebhookEventRequest(gitHubWebhookEventRequest);
            log.info("Got webhook pull request {}", gitHubWebhookEventRequest);
            webhookFacadeService.createQuestionWithWebhookEvent(request);
        }
        return ResponseEntity.ok().build();
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
}
