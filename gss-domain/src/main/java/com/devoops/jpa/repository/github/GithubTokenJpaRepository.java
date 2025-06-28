package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.GithubTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GithubTokenJpaRepository extends JpaRepository<GithubTokenEntity, Long> {

    Optional<GithubTokenEntity> findByUser_Id(long userId);
}
