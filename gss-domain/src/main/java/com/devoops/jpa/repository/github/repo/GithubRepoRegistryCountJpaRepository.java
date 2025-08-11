package com.devoops.jpa.repository.github.repo;

import com.devoops.jpa.entity.github.repo.GithubRepoRegistryCountEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepoRegistryCountJpaRepository extends JpaRepository<GithubRepoRegistryCountEntity, Long> {

    Optional<GithubRepoRegistryCountEntity> findByExternalId(long externalId);

    boolean existsByExternalId(long externalId);

    void deleteByExternalId(long repoExternalId);
}
