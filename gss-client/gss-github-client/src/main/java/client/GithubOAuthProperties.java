package client;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dev-oops.webclient.github.oauth")
public record GithubOAuthProperties(
        String clientId,
        String clientSecret
) {

}
