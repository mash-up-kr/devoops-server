package com.devoops.client;

import com.devoops.dto.request.GithubTokenRequest;
import com.devoops.dto.response.GithubTokenResponse;
import com.devoops.dto.response.UserInfoResponse;
import java.util.Map;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@EnableConfigurationProperties(GithubOAuthProperties.class)
public class GithubOAuthClientImpl implements GithubOAuthClient {

    private final WebClient webClient;
    private final GithubOAuthProperties authProperties;

    public GithubOAuthClientImpl(
            WebClient.Builder webClientBuilder,
            GithubOAuthProperties authProperties
    ) {
        this.webClient = webClientBuilder.build();
        this.authProperties = authProperties;
    }

    @Override
    public GithubTokenResponse getToken(GithubTokenRequest tokenRequest) {
        return webClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "client_id", authProperties.clientId(),
                        "client_secret", authProperties.clientSecret(),
                        "code", tokenRequest.code(),
                        "redirect_uri", tokenRequest.redirect_uri()
                ))
                .retrieve()
                .bodyToMono(GithubTokenResponse.class)
                .block();
    }

    @Override
    public UserInfoResponse getUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://api.github.com/user")
                .header(HttpHeaders.ACCEPT, " application/vnd.github+json")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(UserInfoResponse.class)
                .block();
    }
}
