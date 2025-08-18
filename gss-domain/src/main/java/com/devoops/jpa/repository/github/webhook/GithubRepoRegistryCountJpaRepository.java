package com.devoops.jpa.repository.github.webhook;

import com.devoops.jpa.entity.github.webhook.GithubRepoRegistryCountEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GithubRepoRegistryCountJpaRepository extends JpaRepository<GithubRepoRegistryCountEntity, Long> {

    Optional<GithubRepoRegistryCountEntity> findByExternalId(long externalId);

    boolean existsByExternalId(long externalId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update GithubRepoRegistryCountEntity e set e.registryCount = e.registryCount + 1 where e.externalId = :externalId")
    int incrementByExternalId(@Param("externalId") long externalId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update GithubRepoRegistryCountEntity e set e.registryCount = e.registryCount - 1 where e.externalId = :externalId and e.registryCount > 0")
    int decrementByExternalIdIfPositive(@Param("externalId") long externalId);

    void deleteByExternalId(long repoExternalId);
}
