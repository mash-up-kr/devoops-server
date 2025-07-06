package com.devoops.dto.request;

/**
 * docs : https://docs.github.com/ko/webhooks/webhook-events-and-payloads#pull_request
 */

import java.time.LocalDateTime;
import java.util.List;

/**
 * private final Long id;
 * private final long repositoryId;
 * private final long userId;
 * private final String title;
 * private final String description;
 * private final String summary;
 * private final long externalId;
 * private final RecordStatus recordStatus;
 * private final LocalDateTime mergedAt;
 * private final String tag;
 */

public record GitHubWebhookEventRequest(
        String action,
        int number,
        PullRequest pullRequest,
        Repository repository
) {
    public record PullRequest(
            String url,
            long id,
            String diffUrl,
            int number,
            // open, closed
            String state,
            // PR 제목
            String title,
            // PR 본문
            String body,
            List<Label> labels,
            User user,
            String createdAt,
            String updatedAt,
            String closedAt,
            Boolean merged,
            LocalDateTime mergedAt
    ) {
    }

    public record User(
            String login,
            long id
    ) {
    }

    public record Repository(
            long id,
            String name,
            boolean isPrivate,
            User owner
    ) {
    }

    public record Label(
            long id,
            String name
    ) {
    }

    public long getRepositoryId() {
        return repository.id;
    }

    public Long getUserId() {
        return pullRequest.user.id;
    }

    public String getTitle() {
        return pullRequest.title;
    }

    public String getDescription() {
        return pullRequest.body;
    }

    public long getExternalId() {
        return pullRequest.id;
    }

    public String getTag() {
        return pullRequest.labels.getFirst().name;
    }

    public LocalDateTime getMergedAt() {
        return pullRequest.mergedAt;
    }

    public Boolean isMerged() {
        if (action.equals("closed")) {
            return pullRequest.merged;
        } else {
            return false;
        }
    }

    public String getPullRequestDiffUrl() {
        return pullRequest.diffUrl;
    }
}
