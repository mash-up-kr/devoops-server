package com.devoops.controller;

import com.devoops.controller.auth.AuthUser;
import com.devoops.domain.entity.user.User;
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
        @AuthUser User user,
        @PathVariable long repositoryId
    ) {
        gitHubService.registerWebhook(user, repositoryId);
    }
}
