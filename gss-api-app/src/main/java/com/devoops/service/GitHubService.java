package com.devoops.service;

import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import com.devoops.dto.request.GitHubWebhookRequest;
import com.devoops.dto.request.GithubRepoUrl;
import com.devoops.dto.response.GithubRepoInfoResponse;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
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
    private final GithubTokenDomainRepository githubTokenDomainRepository;

    public void registerWebhook(User user, long repositoryId) {

        GithubRepository githubRepository = githubRepoDomainRepository.findByIdAndUserId(repositoryId, user.getId());
        GithubToken githubToken = githubTokenDomainRepository.findByUserId(user)
                .orElseThrow(() -> new GssException(ErrorCode.NO_RESOURCE_FOUND));

        gitHubClient.createWebhook(
                githubToken.getToken(),
                githubRepository.getOwner(),
                githubRepository.getName(),
                GitHubWebhookRequest.ofPullRequestEvent(mcpWebhookUrl)
        );
    }

    public GithubRepoInfoResponse getRepositoryInfo(GithubRepoUrl repoUrl, GithubToken token) {
        return gitHubClient.getRepositoryInfo(
                token.getToken(),
                repoUrl.getOwner(),
                repoUrl.getRepoName()
        );
    }
}
