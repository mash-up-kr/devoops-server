package client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.client.GithubOAuthClient;
import com.devoops.client.GithubOAuthClientImpl;
import com.devoops.client.GithubOAuthProperties;
import com.devoops.dto.request.GithubTokenRequest;
import com.devoops.dto.response.GithubTokenResponse;
import com.devoops.dto.response.UserInfoResponse;
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

class GithubOAuthClientTest {

    private GithubOAuthClient githubOAuthClient;
    private ExchangeFunction mockExchangeFunction;

    @BeforeEach
    void setUp() {
        GithubOAuthProperties authProperties = new GithubOAuthProperties("testClientId", "testClientSecret");
        mockExchangeFunction = Mockito.mock(ExchangeFunction.class);
        WebClient.Builder webClientBuilder = WebClient.builder()
                .exchangeFunction(mockExchangeFunction);
        githubOAuthClient = new GithubOAuthClientImpl(webClientBuilder, authProperties);
    }

    @Nested
    class BindResult {

        @Test
        void 액세스_토큰을_가져올_수_있다() throws IOException {
            GithubTokenRequest githubTokenRequest = new GithubTokenRequest("testCode", "testRedirectUrl");
            String expectedAccessToken = "example_access_token";
            mockClient(HttpStatus.OK, "github-api-response/tokenSuccess.json");

            GithubTokenResponse tokenResponse = githubOAuthClient.getToken(githubTokenRequest);

            assertThat(tokenResponse.accessToken()).isEqualTo(expectedAccessToken);
        }

        @Test
        void 유저_정보를_가져올_수_있다() throws IOException {
            long expectedProviderId = 148152234L;
            String expectedNickname = "김건우";
            String expectedProfileImageUrl = "https://avatars.githubusercontent.com/u/148152234?v=4";
            mockClient(HttpStatus.OK, "github-api-response/userInfoSuccess.json");

            UserInfoResponse userInfoResponse = githubOAuthClient.getUserInfo("testAccessToken");

            assertAll(
                    () -> assertThat(userInfoResponse.id()).isEqualTo(expectedProviderId),
                    () -> assertThat(userInfoResponse.name()).isEqualTo(expectedNickname),
                    () -> assertThat(userInfoResponse.avatarUrl()).isEqualTo(expectedProfileImageUrl)
            );
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
