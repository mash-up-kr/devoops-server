package com.devoops.client;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface GitHubClient {

    /**
     * GitHub 저장소에 Webhook을 등록합니다.
     *
     * @param authorization Authorization 헤더 (예: Bearer 토큰)
     * @param owner         저장소 소유자
     * @param repo          저장소 이름
     * @param request       등록 요청 본문
     * @see <a href="https://docs.github.com/ko/rest/repos/webhooks?apiVersion=2022-11-28#create-a-repository-webhook">
     * GitHub 공식 문서 - Create a repository webhook
     * </a>
     */
    @PostExchange(url = "/repos/{owner}/{repo}/hooks")
    void createWebhook(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
        @PathVariable("owner") String owner,
        @PathVariable("repo") String repo,
        @RequestBody GitHubWebhookRequest request
    );
}
