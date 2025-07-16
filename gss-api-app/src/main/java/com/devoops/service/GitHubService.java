package com.devoops.service;

import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.github.GithubWebhook;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import com.devoops.domain.repository.github.GithubWebhookDomainRepository;
import com.devoops.dto.request.GitHubWebhookRequest;
import com.devoops.dto.request.GithubRepoUrl;
import com.devoops.dto.response.GithubPrResponse;
import com.devoops.dto.response.GithubRepoInfoResponse;
import com.devoops.dto.response.WebHookCreateResponse;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.repository.github.GithubWebHookDomainRepositoryImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GitHubService {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final int MAX_USER_PR_LIMIT = 3;
    private static final int MAX_PER_PAGE_PULL_REQUEST = 10;
    private static final String REQUEST_PULL_REQUEST_STATUS = "closed";

    @Value("${dev-oops.mcp.webhook-url}")
    private String mcpWebhookUrl;

    private final GitHubClient gitHubClient;
    private final GithubRepoDomainRepository githubRepoDomainRepository;
    private final GithubTokenDomainRepository githubTokenDomainRepository;
    private final GithubWebhookDomainRepository githubWebhookDomainRepository;

    public void registerWebhook(User user, long repositoryId) {

        GithubRepository githubRepository = githubRepoDomainRepository.findByIdAndUserId(repositoryId, user.getId());
        GithubToken githubToken = githubTokenDomainRepository.findByUserId(user)
                .orElseThrow(() -> new GssException(ErrorCode.NO_RESOURCE_FOUND));
        createWebHook(githubToken, githubRepository);
    }

    private GithubWebhook createWebHook(GithubToken token, GithubRepository repo) {
        WebHookCreateResponse webHookCreateResponse = gitHubClient.createWebhook(
                BEARER_PREFIX + token.getToken(),
                repo.getOwner(),
                repo.getName(),
                GitHubWebhookRequest.ofPullRequestEvent(mcpWebhookUrl)
        );
        GithubWebhook webhook = new GithubWebhook(webHookCreateResponse.id(), repo.getId());
        return githubWebhookDomainRepository.save(webhook);
    }

    public List<GithubPrResponse> getUserPullRequests(
            GithubRepoUrl repoUrl,
            User user
    ) {
        GithubToken token = user.getGithubToken();
        List<GithubPrResponse> closedPullRequests = gitHubClient.getPullRequests(
                BEARER_PREFIX + token.getToken(),
                repoUrl.getOwner(),
                repoUrl.getRepoName(),
                REQUEST_PULL_REQUEST_STATUS,
                MAX_PER_PAGE_PULL_REQUEST,
                1
        );
        System.out.println(closedPullRequests);
        return closedPullRequests.stream()
                .filter(pr -> pr.isUserPr(user.getProviderId()))
                .limit(MAX_USER_PR_LIMIT)
                .toList();
    }

    public GithubRepoInfoResponse getRepositoryInfo(GithubRepoUrl repoUrl, GithubToken token) {
        return gitHubClient.getRepositoryInfo(
                BEARER_PREFIX + token.getToken(),
                repoUrl.getOwner(),
                repoUrl.getRepoName()
        );
    }

    @Transactional
    public void deleteWebhook(User user, long repositoryId) {
        GithubRepository repo = githubRepoDomainRepository.findByIdAndUserId(repositoryId, user.getId());
        GithubToken githubToken = githubTokenDomainRepository.findByUserId(user)
                .orElseThrow(() -> new GssException(ErrorCode.NO_RESOURCE_FOUND));
        GithubWebhook webhook = githubWebhookDomainRepository.findByRepositoryId(repo.getId());
        gitHubClient.deleteWebhook(
                BEARER_PREFIX + githubToken.getToken(),
                repo.getOwner(),
                repo.getName(),
                webhook.getExternalId()
        );
        githubWebhookDomainRepository.deleteById(webhook.getId());
    }
}
