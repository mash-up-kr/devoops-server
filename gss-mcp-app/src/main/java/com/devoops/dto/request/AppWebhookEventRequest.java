package com.devoops.dto.request;

import java.time.LocalDateTime;

public record AppWebhookEventRequest(
        Boolean isMerged,
        long pullRequestId,
        String diffUrl,
        String title,
        String description,
        String label,
        long repositoryId,
        long userId,
        LocalDateTime mergedAt
) {

    public AppWebhookEventRequest(GitHubWebhookEventRequest request) {
        this(
                request.isMerged(),
                request.getExternalId(),
                request.getPullRequestDiffUrl(),
                request.getTitle(),
                request.getDescription(),
                request.getTag(),
                request.getRepositoryId(),
                request.getUserId(),
                request.getMergedAt()
        );
    }
}
