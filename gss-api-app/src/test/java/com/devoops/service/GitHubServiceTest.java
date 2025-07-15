package com.devoops.service;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.dto.request.GithubRepoUrl;
import com.devoops.dto.response.GithubPrResponse;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GitHubServiceTest {

    @Autowired
    GitHubService gitHubService;

    @Test
    @Disabled
    void getUserPullRequests() {
        String token = "";
        List<GithubPrResponse> userPullRequests = gitHubService.getUserPullRequests(
                new GithubRepoUrl("https://github.com/coli-geonwoo/prography-ping-pong"),
                new GithubToken(token),
                148152234
        );
        System.out.println(userPullRequests);
    }
}
