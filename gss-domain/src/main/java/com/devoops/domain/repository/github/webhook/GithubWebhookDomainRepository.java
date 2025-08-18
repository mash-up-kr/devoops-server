package com.devoops.domain.repository.github.webhook;

import com.devoops.domain.entity.github.webhook.GithubWebhook;

public interface GithubWebhookDomainRepository {

    GithubWebhook save(GithubWebhook webHook);

    GithubWebhook findByRepositoryId(long repositoryId);

    void deleteById(long repositoryId);
}
