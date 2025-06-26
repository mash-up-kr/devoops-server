package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.GithubRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GithubRepoJpaRepository extends JpaRepository<GithubRepositoryEntity, Long> {

    boolean existsByIdAndUser_Id(Long id, Long userId);
}
