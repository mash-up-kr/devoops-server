package com.devoops.service;

import com.devoops.client.GitHubClient;
import com.devoops.client.GitHubWebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubService {

    @Value("${dev-oops.mcp.webhook-url}")
    private String mcpWebhookUrl;

    private final GitHubClient gitHubClient;

    public void registerWebhook(long userId, long repositoryId) {

        // TODO: userId, repositoryId를 통해 GitHub owner/repo name 조회
        String owner = "mock-owner";
        String repo = "mock-repo";

        // TODO: repository에 연결된 GitHub access token 조회
        String accessToken = "mock-github-token";

        GitHubWebhookRequest request = GitHubWebhookRequest.createDefault(mcpWebhookUrl);
        gitHubClient.createWebhook(accessToken, owner, repo, request);
    }
}
