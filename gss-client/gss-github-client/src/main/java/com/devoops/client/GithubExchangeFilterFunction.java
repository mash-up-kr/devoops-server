package com.devoops.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
public class GithubExchangeFilterFunction {

    @Bean
    public ExchangeFilterFunction githubErrorLogger() {
        return (request, next) -> next.exchange(request)
            .flatMap(response -> {
                if (response.statusCode().isError()) {
                    return response.bodyToMono(String.class).flatMap(body -> {
                        log.error("GitHub API Error: {}", Map.of(
                            "method", request.method(),
                            "url", request.url(),
                            "status", response.statusCode(),
                            "response", body
                        ));
                        // TODO: 커스텀한 예외로 변경 필요
                        return Mono.error(new RuntimeException("GitHub API error: " + body));
                    });
                }

                return Mono.just(response);
            });
    }
}
