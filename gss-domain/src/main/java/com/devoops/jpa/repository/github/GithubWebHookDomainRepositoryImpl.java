package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.GithubWebhook;
import com.devoops.domain.repository.github.GithubWebhookDomainRepository;
import com.devoops.jpa.entity.github.GithubWebhookEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GithubWebHookDomainRepositoryImpl implements GithubWebhookDomainRepository {

    private final GithubWebHookJpaRepository webHookRepository;

    public GithubWebhook save(GithubWebhook webHook) {
        GithubWebhookEntity webHookEntity = GithubWebhookEntity.from(webHook);
        return webHookRepository.save(webHookEntity)
                .toDomainEntity();
    }

    public void deleteByRepositoryId(String repositoryId) {
        webHookRepository.deleteByRepositoryId(repositoryId);
    }
}
