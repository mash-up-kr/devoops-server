package com.devoops.jpa.repository.github.webhook;

import com.devoops.jpa.entity.github.webhook.GithubWebhookEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubWebHookJpaRepository extends JpaRepository<GithubWebhookEntity, Long> {

    Optional<GithubWebhookEntity> findByRepositoryId(long repositoryId);
}
