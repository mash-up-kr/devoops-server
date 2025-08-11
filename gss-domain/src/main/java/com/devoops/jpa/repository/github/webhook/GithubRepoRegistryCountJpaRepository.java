package com.devoops.jpa.repository.github.webhook;

import com.devoops.jpa.entity.github.webhook.GithubRepoRegistryCountEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepoRegistryCountJpaRepository extends JpaRepository<GithubRepoRegistryCountEntity, Long> {

    Optional<GithubRepoRegistryCountEntity> findByExternalId(long externalId);

    boolean existsByExternalId(long externalId);

    void deleteByExternalId(long repoExternalId);
}
