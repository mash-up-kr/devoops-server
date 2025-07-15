package com.devoops.event.listener;

import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.GithubRepoUrl;
import com.devoops.dto.response.GithubPrResponse;
import com.devoops.event.AnalyzeMyPrEvent;
import com.devoops.service.GitHubService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrAnalyzeListener {

    private final GitHubService gitHubService;

    @Async
    @EventListener
    public void analyzeMyPullRequests(AnalyzeMyPrEvent analyzeMyPrEvent) {
        GithubRepoUrl repoUrl = analyzeMyPrEvent.getRepoUrl();
        User user = analyzeMyPrEvent.getUser();
        List<GithubPrResponse> userPullRequests = gitHubService.getUserPullRequests(repoUrl, user);
        //Redis Pub 이벤트 발행
    }
}
