package com.devoops.service;

import com.devoops.client.GitHubClient;
import com.devoops.client.GitHubWebhookRequest;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GitHubService {

    @Value("${dev-oops.mcp.webhook-url}")
    private String mcpWebhookUrl;

    private final GitHubClient gitHubClient;
    private final GithubRepoDomainRepository githubRepoDomainRepository;

    public void registerWebhook(User user, long repositoryId) {

        GithubRepository githubRepository = githubRepoDomainRepository.findByIdAndUserId(repositoryId, user.getId());

        // TODO: repository에 연결된 GitHub access token 조회
        String accessToken = "mock-github-token";

        GitHubWebhookRequest request = GitHubWebhookRequest.ofPullRequestEvent(mcpWebhookUrl);
        gitHubClient.createWebhook(accessToken, githubRepository.getOwner(), githubRepository.getName(), request);
    }
}
