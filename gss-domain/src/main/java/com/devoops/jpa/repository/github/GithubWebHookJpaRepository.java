package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.GithubWebhookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubWebHookJpaRepository extends JpaRepository<GithubWebhookEntity, Long> {

    void deleteByRepositoryId(String repositoryId);
}
