package client;

import dto.request.GithubTokenRequest;
import dto.response.GithubTokenResponse;
import dto.response.UserInfoResponse;
import java.util.Map;
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
        this.webClient = webClientBuilder.build();
        this.authProperties = authProperties;
    }

    public Mono<GithubTokenResponse> getAccessToken(GithubTokenRequest tokenRequest) {
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
                .bodyToMono(GithubTokenResponse.class);
    }

    public Mono<UserInfoResponse> getUserInfo(String accessToken) {
        return webClient.get()
                .uri("https://api.github.com/user")
                .header(HttpHeaders.ACCEPT, " application/vnd.github+json")
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(UserInfoResponse.class);
    }
}
