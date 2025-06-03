package client;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dev-oops.webclient.github")
public record GithubWebClientProperties(
        String url,
        int connectTimeout,
        int readTimeout
) {

}
