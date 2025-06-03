package client;

import dto.request.GithubTokenRequest;
import dto.response.GithubTokenResponse;
import dto.response.UserInfoResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@EnableConfigurationProperties(GithubOAuthProperties.class)
public class GithubOAuthClient {

    private final WebClient webClient;
    private final GithubOAuthProperties authProperties;

    public GithubOAuthClient(
            WebClient.Builder webClientBuilder,
            GithubOAuthProperties authProperties
    ) {
        this.webClient = WebClient.builder().build();
        this.authProperties = authProperties;
    }

    public Mono<GithubTokenResponse> getAccessToken(GithubTokenRequest tokenRequest) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("https://github.com/login/oauth/access_token")
                        .queryParam("client_id", authProperties.clientId())
                        .queryParam("client_secret", authProperties.clientSecret())
                        .queryParam("code", tokenRequest.code())
                        .queryParam("redirect_uri", tokenRequest.redirect_uri())
                        .build()
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(GithubTokenResponse.class);
    }

    public Mono<UserInfoResponse> getUserInfo(String accessToken) {
        return webClient.post()
                .uri("https://api.github.com/user")
                .header(HttpHeaders.ACCEPT, " application/vnd.github+json")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(UserInfoResponse.class);
    }
}
