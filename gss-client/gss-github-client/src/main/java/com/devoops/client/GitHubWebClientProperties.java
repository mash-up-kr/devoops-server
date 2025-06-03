package com.devoops.client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dev-oops.webclient.github")
public record GitHubWebClientProperties(
    String url,
    int connectTimeout,
    int readTimeout
) {
}
