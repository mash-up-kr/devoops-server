package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.GithubRepositoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepoJpaRepository extends JpaRepository<GithubRepositoryEntity, Long> {

}
