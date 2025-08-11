package com.devoops.jpa.repository.github.repo;

import com.devoops.jpa.entity.github.repo.GithubRepoRegistryCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepoRegistryCountJpaRepository extends JpaRepository<GithubRepoRegistryCountEntity, Long> {

}
