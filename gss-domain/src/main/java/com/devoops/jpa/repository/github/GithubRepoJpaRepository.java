package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.GithubRepositoryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepoJpaRepository extends JpaRepository<GithubRepositoryEntity, Long> {

    boolean existsByIdAndUserId(Long id, Long userId);

    Optional<GithubRepositoryEntity> findByIdAndUserId(Long id, Long userId);

    List<GithubRepositoryEntity> findAllByUserId(Long userId);

    boolean existsByGithubRepositoryId(long externalId);

    Optional<GithubRepositoryEntity> findByGithubRepositoryId(long externalId);
}
