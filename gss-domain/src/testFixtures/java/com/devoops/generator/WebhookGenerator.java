package com.devoops.generator;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.GithubWebhook;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubWebhookDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebhookGenerator {

    @Autowired
    private GithubWebhookDomainRepository githubWebhookDomainRepository;

    public GithubWebhook generate(User user, GithubRepository repository) {
        GithubWebhook webHook = new GithubWebhook(101L, repository.getId());
        return githubWebhookDomainRepository.save(webHook);
    }
}
