package com.devoops.controller.webhook;

import com.devoops.dto.request.AppWebhookEventRequest;
import com.devoops.dto.request.GitHubWebhookEventRequest;
import com.devoops.service.webhook.WebhookFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final WebhookFacadeService webhookFacadeService;

    /**
     * webhook url : https://mcp.dev-oops.kr/api/webhooks/github/pull-requests
     */
    @PostMapping("/github/pull-requests")
    public ResponseEntity<Void> getWebhookPullRequest(
            @RequestBody GitHubWebhookEventRequest gitHubWebhookEventRequest
    ) {
        AppWebhookEventRequest request = new AppWebhookEventRequest(gitHubWebhookEventRequest);
        webhookFacadeService.createQuestionWithWebhookEvent(request);
        return ResponseEntity.ok().build();
    }
}
