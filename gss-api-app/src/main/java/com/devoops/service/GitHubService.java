package com.devoops.service;

import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.GithubRepoUrl;
import com.devoops.dto.response.GithubPrResponse;
import com.devoops.dto.response.GithubRepoInfoResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubService {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final int MAX_USER_PR_LIMIT = 3;
    private static final int MAX_PER_PAGE_PULL_REQUEST = 10;
    private static final String REQUEST_PULL_REQUEST_STATUS = "closed";

    private final GitHubClient gitHubClient;

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
        return closedPullRequests.stream()
                .filter(pr -> pr.isUserPr(user.getProviderId()) && pr.mergedAt() != null)
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
}
