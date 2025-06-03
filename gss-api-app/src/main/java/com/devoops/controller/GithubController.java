package com.devoops.controller;

import com.devoops.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/github")
public class GithubController {

    private final GitHubService gitHubService;

    @PostMapping("/repositories/{repositoryId}/webhooks")
    public void registerWebhook(
        @PathVariable long repositoryId
    ) {

        // TODO: 인증 정보를 기반으로 userId 추출 (ex: SecurityContext, JWT 등)
        var userId = 1L;
        gitHubService.registerWebhook(userId, repositoryId);
    }
}
