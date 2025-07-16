package com.devoops.dto;

import java.time.LocalDateTime;

public record AppWebhookEventRequest(
        Boolean isMerged,
        long pullRequestId,
        String pullRequestUrl,
        String diffUrl,
        String title,
        String description,
        String label,
        long repositoryId,
        long userId,
        LocalDateTime mergedAt
) {

    public GithubPRUrl getParsedUrl() {
        return new GithubPRUrl(pullRequestUrl);
    }
}
