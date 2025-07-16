package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.GithubWebhook;

public interface GithubWebhookDomainRepository {

    GithubWebhook save(GithubWebhook webHook);

    GithubWebhook findByRepositoryId(long repositoryId);

    void deleteById(long repositoryId);
}
