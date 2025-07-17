package com.devoops.client;

import com.devoops.exception.GithubTimeoutException;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import io.netty.handler.timeout.TimeoutException;
import java.net.URI;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubWebclient {

    private final WebClient webClient;

    public String getCodeChangeHistory(String diffUrl, String token) {
        log.info("[GithubClient] Requesting diff from {}", diffUrl);
        String githubApiDiffUrl = convertDiffUrlToApiUrl(diffUrl);

        return webClient.get()
                .uri(githubApiDiffUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .accept(MediaType.parseMediaType("application/vnd.github.v3.diff"))
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(3))
                .onErrorMap(TimeoutException.class, ex -> {
                    log.error("[GithubClient] ReadTimeoutException occurred for diff url: {}", diffUrl);
                    return new GithubTimeoutException("코드 변경 이력을 가져오는 중 요청 시간 초과", ex);
                })
                .block();
    }

    public String convertDiffUrlToApiUrl(String browserDiffUrl) {
        try {
            URI uri = new URI(browserDiffUrl);
            String[] parts = uri.getPath().split("/");
            if (parts.length >= 5 && "pull".equals(parts[3])) {
                String owner = parts[1];
                String repo = parts[2];
                String pullNumberWithExtension = parts[4]; // 38.diff
                String pullNumber = pullNumberWithExtension.replace(".diff", "");

                return String.format("https://api.github.com/repos/%s/%s/pulls/%s", owner, repo, pullNumber);
            }
        } catch (Exception e) {
            throw new GssException(ErrorCode.GITHUB_CLIENT_ERROR);
        }
        throw new GssException(ErrorCode.GITHUB_CLIENT_ERROR);
    }

}
