package com.devoops.client;

import com.devoops.dto.response.UserInfoResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GithubOAuthClientImpl implements GithubOAuthClient {

    private final WebClient webClient;

    public GithubOAuthClientImpl(
            WebClient.Builder webClientBuilder
    ) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public UserInfoResponse getUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://api.github.com/user")
                .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(UserInfoResponse.class)
                .block();
    }
}
