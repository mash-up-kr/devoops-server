package com.devoops.generator;

import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.webhook.GithubRepoRegistryCount;
import com.devoops.domain.entity.github.webhook.GithubWebhook;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.webhook.GithubRepoRegistryCountRepository;
import com.devoops.domain.repository.github.webhook.GithubWebhookDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebhookGenerator {

    @Autowired
    private GithubWebhookDomainRepository githubWebhookDomainRepository;

    @Autowired
    private GithubRepoRegistryCountRepository githubRepoRegistryCountRepository;

    public GithubWebhook generate(User user, GithubRepository repository, long trackingCount) {
        GithubWebhook webHook = new GithubWebhook(101L, repository.getId());
        githubRepoRegistryCountRepository.save(new GithubRepoRegistryCount(repository.getExternalId(), trackingCount));
        return githubWebhookDomainRepository.save(webHook);
    }
}
