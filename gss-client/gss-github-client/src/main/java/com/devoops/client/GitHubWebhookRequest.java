package com.devoops.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record GitHubWebhookRequest(
    String name,
    boolean active,
    List<String> events,
    GitHubWebhookConfig config
) {
    public record GitHubWebhookConfig(
        String url,
        @JsonProperty("content_type")
        String contentType
    ) {
    }

    public static GitHubWebhookRequest createDefault(String slackWebhookUrl) {
        return new GitHubWebhookRequest(
            "web",
            true,
            List.of("pull_request"),
            new GitHubWebhookConfig(
                slackWebhookUrl,
                "json"
            )
        );
    }
}
