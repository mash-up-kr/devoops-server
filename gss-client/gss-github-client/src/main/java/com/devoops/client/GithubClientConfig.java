package com.devoops.client;

import io.netty.channel.ChannelOption;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GitHubWebClientProperties.class)
public class GithubClientConfig {

    private final GitHubWebClientProperties properties;
    private final ExchangeFilterFunction githubErrorDecoder;

    @Bean
    public GitHubClient gitHubApiClient() {

        return HttpServiceProxyFactory
            .builderFor(WebClientAdapter.create(createGitHubWebClient()))
            .build()
            .createClient(GitHubClient.class);
    }

    private WebClient createGitHubWebClient() {
        return WebClient.builder()
            .baseUrl(properties.url())
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, properties.connectTimeout())
                    .responseTimeout(Duration.ofMillis(properties.readTimeout()))
            ))
            .filter(githubErrorDecoder)
            .build();
    }
}
