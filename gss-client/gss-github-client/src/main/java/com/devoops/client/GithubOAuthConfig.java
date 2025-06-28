package com.devoops.client;

import io.netty.channel.ChannelOption;
import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class GithubOAuthConfig {

    private static final Duration DEFAULT_RESPONSE_TIMEOUT = Duration.ofSeconds(2L);
    private static final int CONNECTION_TIMEOUT_MILLS = 5000;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient()));
    }

    private HttpClient httpClient() {
        return HttpClient.create()
                .responseTimeout(DEFAULT_RESPONSE_TIMEOUT)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECTION_TIMEOUT_MILLS);
    }
}
