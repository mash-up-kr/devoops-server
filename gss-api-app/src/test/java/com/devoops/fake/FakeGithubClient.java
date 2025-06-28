package com.devoops.fake;

import com.devoops.client.GitHubClient;
import com.devoops.dto.request.GitHubWebhookRequest;
import com.devoops.dto.response.GithubRepoInfoResponse;
import com.devoops.dto.response.OwnerResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeGithubClient implements GitHubClient {

    @Override
    public GithubRepoInfoResponse getRepositoryInfo(String authorization, String owner, String repo) {
        return new GithubRepoInfoResponse(
                123,
                "testRepo",
                "testUrl",
                new OwnerResponse("owner")
        );
    }

    @Override
    public void createWebhook(String authorization, String owner, String repo, GitHubWebhookRequest request) {
    }
}
