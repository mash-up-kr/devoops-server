package com.devoops.event.listener;

import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.dto.request.GithubRepoUrl;
import com.devoops.dto.response.GithubPrResponse;
import com.devoops.event.AnalyzeMyPrEvent;
import com.devoops.event.publisher.PrAnalysisPublisher;
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
    private final PrAnalysisPublisher prAnalysisPublisher;

    @Async
    @EventListener
    public void analyzeMyPullRequests(AnalyzeMyPrEvent analyzeMyPrEvent) {
        GithubRepoUrl repoUrl = analyzeMyPrEvent.getRepoUrl();
        User user = analyzeMyPrEvent.getUser();
        List<GithubPrResponse> userPullRequests = gitHubService.getUserPullRequests(repoUrl, user);

        //Redis Pub 이벤트 발행
        List<AppWebhookEventRequest> requests = userPullRequests.stream()
                .map(this::createAppWebhookEventRequest)
                .toList();
        prAnalysisPublisher.publish(requests);
    }

    private AppWebhookEventRequest createAppWebhookEventRequest(GithubPrResponse githubPrResponse) {
        return new AppWebhookEventRequest(
                true,
                githubPrResponse.id(),
                githubPrResponse.url(),
                githubPrResponse.diffUrl(),
                githubPrResponse.title(),
                githubPrResponse.getDescription(),
                githubPrResponse.getTag(),
                githubPrResponse.getRepositoryId(),
                githubPrResponse.getUserId(),
                githubPrResponse.mergedAt()
        );
    }
}
