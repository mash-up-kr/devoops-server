package com.devoops.fake;

import com.devoops.client.GitHubClient;
import com.devoops.dto.request.GitHubWebhookRequest;
import com.devoops.dto.response.GithubPrResponse;
import com.devoops.dto.response.GithubPrResponse.Label;
import com.devoops.dto.response.GithubPrResponse.Repository;
import com.devoops.dto.response.GithubRepoInfoResponse;
import com.devoops.dto.response.OwnerResponse;
import com.devoops.dto.response.WebHookCreateResponse;
import java.time.LocalDateTime;
import java.util.List;
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
    public List<GithubPrResponse> getPullRequests(String authorization, String owner, String repo, String state, long perPage, long page) {
        return List.of(new GithubPrResponse(
                1L,
                "https://api.github.com/repos/owner/repo/pulls/38",
                "title",
                "body",
                new GithubPrResponse.User(1L, "login"),
                new GithubPrResponse.Head(new Repository(1L, "login")),
                List.of(new Label(1L, "feat")),
                "diffUrl",
                LocalDateTime.now()
        ));
    }

    @Override
    public WebHookCreateResponse createWebhook(String authorization, String owner, String repo, GitHubWebhookRequest request) {
        return new WebHookCreateResponse(101L);
    }
}
