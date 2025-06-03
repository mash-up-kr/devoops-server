package client;

import dto.request.GithubTokenRequest;
import dto.response.GithubTokenResponse;
import dto.response.UserInfoResponse;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class GithubOAuthClientTest {

    private GithubOAuthClient githubOAuthClient;
    private ExchangeFunction mockExchangeFunction;

    @BeforeEach
    void setUp() {
        GithubOAuthProperties authProperties = new GithubOAuthProperties("testClientId", "testClientSecret");
        mockExchangeFunction = Mockito.mock(ExchangeFunction.class);
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(mockExchangeFunction);
        githubOAuthClient = new GithubOAuthClient(webClientBuilder, authProperties);
    }

    @Nested
    class BindResult {

        @Test
        void 액세스_토큰을_가져올_수_있다() throws IOException {
            GithubTokenRequest githubTokenRequest = new GithubTokenRequest("testCode", "testRedirectUrl");
            String expectedAccessToken = "example_access_token";
            mockClient(HttpStatus.OK, "github-api-response/tokenSuccess.json");

            Mono<GithubTokenResponse> tokenResponse = githubOAuthClient.getAccessToken(githubTokenRequest);

            StepVerifier.create(tokenResponse)
                    .expectNextMatches(response -> response.accessToken().equals(expectedAccessToken))
                    .verifyComplete();
        }

        @Test
        void 유저_정보를_가져올_수_있다() throws IOException {
            String expectedEmail = "kkwoo001021@naver.com";
            mockClient(HttpStatus.OK, "github-api-response/userInfoSuccess.json");

            Mono<UserInfoResponse> userInfoResponseMono = githubOAuthClient.getUserInfo("testAccessToken");

            StepVerifier.create(userInfoResponseMono)
                    .expectNextMatches(response -> response.email().equals(expectedEmail))
                    .verifyComplete();
        }

        private void mockClient(HttpStatus status, String responsePath) throws IOException {
            String responseBody = makeResponseByPath(responsePath);
            Mockito.when(mockExchangeFunction.exchange(Mockito.any()))
                    .thenReturn(Mono.just(ClientResponse.create(status)
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .body(responseBody)
                            .build())
                    );
        }

        private String makeResponseByPath(String path) throws IOException {
            return new String(Files.readAllBytes(
                    new ClassPathResource(path).getFile().toPath())
            );
        }
    }
}
