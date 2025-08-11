package com.devoops.jpa.repository.github.repo;

import com.devoops.domain.entity.github.repo.GithubRepoRegistryCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface GithubRepoRegistryCountJpaRepository extends JpaRepository<GithubRepoRegistryCount, Long> {

}
