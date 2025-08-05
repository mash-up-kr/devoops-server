package com.devoops.client;

import com.devoops.exception.GithubNotFoundException;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

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
                            if (response.statusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                                return Mono.error(new GithubNotFoundException("깃허브에서 자원을 찾을 수 없습니다."));
                            }
                            return Mono.error(new GssException(ErrorCode.GITHUB_CLIENT_ERROR));
                        });
                    }

                    return Mono.just(response);
                });
    }
}
