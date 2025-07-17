package com.devoops.dto.request;

/**
 * docs : https://docs.github.com/ko/webhooks/webhook-events-and-payloads#pull_request
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public record GitHubWebhookEventRequest(
        String action,
        int number,
        @JsonProperty("pull_request") PullRequest pullRequest,
        Repository repository
) {
    public record PullRequest(
            String url,
            long id,
            @JsonProperty("diff_url") String diffUrl,
            // open, closed
            String state,
            // PR 제목
            String title,
            // PR 본문
            String body,
            List<Label> labels,
            User user,
            Boolean merged,
            @JsonProperty("merged_at") LocalDateTime mergedAt
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
            @JsonProperty("private") boolean isPrivate,
            User owner
    ) {
    }

    public record Label(
            long id,
            String name
    ) {
    }

    public String getPullRequestUrl() {
        return pullRequest.url;
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
        if (pullRequest.labels == null || pullRequest.labels.isEmpty()) {
            return "NONE";
        }
        return pullRequest.labels.getFirst().name;
    }

    public LocalDateTime getMergedAt() {
        return pullRequest.mergedAt;
    }

    public Boolean isMerged() {
        if (action.equals("closed") && pullRequest.merged != null && pullRequest.mergedAt != null) {
            return pullRequest.merged;
        } else {
            return false;
        }
    }

    public String getPullRequestDiffUrl() {
        return pullRequest.diffUrl;
    }
}
