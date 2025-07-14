package com.devoops.client;

import com.devoops.exception.GithubTimeoutException;
import io.netty.handler.timeout.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubWebclient {
    private final WebClient webClient;

    public String getCodeChangeHistory(String diffUrl, String token) {
        return webClient.get()
                .uri(diffUrl)
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
}
