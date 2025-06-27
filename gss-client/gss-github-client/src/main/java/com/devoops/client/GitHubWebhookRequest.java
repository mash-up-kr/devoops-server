package com.devoops.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.MediaType;

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

    public static GitHubWebhookRequest ofPullRequestEvent(String webhookUrl) {
        return new GitHubWebhookRequest(
            "web",
            true,
            List.of("pull_request"),
            new GitHubWebhookConfig(
                webhookUrl,
                MediaType.APPLICATION_JSON.getSubtype()
            )
        );
    }
}
