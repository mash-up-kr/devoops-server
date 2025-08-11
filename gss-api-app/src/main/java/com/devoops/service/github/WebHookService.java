package com.devoops.service.github;

import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.github.webhook.GithubRepoRegistryCount;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.domain.entity.github.webhook.GithubWebhook;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.repo.GithubRepoDomainRepository;
import com.devoops.domain.repository.github.webhook.GithubRepoRegistryCountRepository;
import com.devoops.domain.repository.github.token.GithubTokenDomainRepository;
import com.devoops.domain.repository.github.webhook.GithubWebhookDomainRepository;
import com.devoops.dto.request.GitHubWebhookRequest;
import com.devoops.dto.response.WebHookCreateResponse;
import com.devoops.exception.GithubNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebHookService {

    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${dev-oops.mcp.webhook-url}")
    private String mcpWebhookUrl;

    private final GitHubClient gitHubClient;
    private final GithubRepoDomainRepository githubRepoDomainRepository;
    private final GithubTokenDomainRepository githubTokenDomainRepository;
    private final GithubWebhookDomainRepository githubWebhookDomainRepository;
    private final GithubRepoRegistryCountRepository registryCountRepository;

    public void registerWebhook(User user, long repositoryId) {
        GithubRepository githubRepository = githubRepoDomainRepository.findByIdAndUserId(repositoryId, user.getId());
        long repoExternalId = githubRepository.getExternalId();
        if(registryCountRepository.existsByExternalId(repoExternalId)) {
            registryCountRepository.plusCount(repoExternalId);
            return;
        }
        registryCountRepository.save(new GithubRepoRegistryCount(repoExternalId, 1L));
        GithubToken githubToken = githubTokenDomainRepository.getByUserId(user.getId());
        createWebhook(githubToken, githubRepository);
    }

    private void createWebhook(GithubToken token, GithubRepository repo) {
        WebHookCreateResponse webHookCreateResponse = gitHubClient.createWebhook(
                BEARER_PREFIX + token.getToken(),
                repo.getOwner(),
                repo.getName(),
                GitHubWebhookRequest.ofPullRequestEvent(mcpWebhookUrl)
        );
        GithubWebhook webhook = new GithubWebhook(webHookCreateResponse.id(), repo.getId());
        githubWebhookDomainRepository.save(webhook);
    }

    @Transactional
    public void deleteWebhook(User user, long repositoryId) {
        GithubRepository repo = githubRepoDomainRepository.findByIdAndUserId(repositoryId, user.getId());
        long repoExternalId = repo.getExternalId();
        GithubRepoRegistryCount registryCount = registryCountRepository.getByExternalId(repoExternalId);

        if(registryCount.greaterThan(1L)) {
            registryCountRepository.minusCount(repoExternalId);
            return;
        }
        GithubToken githubToken = githubTokenDomainRepository.getByUserId(user.getId());
        GithubWebhook webhook = githubWebhookDomainRepository.findByRepositoryId(repo.getId());
        tryDeleteWebhook(githubToken, webhook, repo);
        githubWebhookDomainRepository.deleteById(webhook.getId());
        registryCountRepository.deleteByExternalId(repoExternalId);
    }

    private void tryDeleteWebhook(GithubToken githubToken, GithubWebhook webhook, GithubRepository repo) {
        try {
            gitHubClient.deleteWebhook(
                    BEARER_PREFIX + githubToken.getToken(),
                    repo.getOwner(),
                    repo.getName(),
                    webhook.getExternalId()
            );
        } catch (GithubNotFoundException githubNotFoundException) {
            log.error("깃허브 레포에서 웹훅을 찾을 수 없습니다 : {}, repo : {} ", githubNotFoundException, repo.getName());
        }
    }
}
