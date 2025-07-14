package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.jpa.entity.github.GithubRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GithubRepoJpaRepository extends JpaRepository<GithubRepositoryEntity, Long> {

    boolean existsByIdAndUser_Id(Long id, Long userId);

    Optional<GithubRepositoryEntity> findByIdAndUser_Id(Long id, Long userId);

    List<GithubRepositoryEntity> findAllByUser_Id(Long userId);

    boolean existsByGithubRepositoryId(long externalId);

    Optional<GithubRepositoryEntity> findByGithubRepositoryId(long externalId);
}
