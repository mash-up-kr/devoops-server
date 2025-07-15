package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.GithubTokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubTokenJpaRepository extends JpaRepository<GithubTokenEntity, Long> {

    Optional<GithubTokenEntity> findByUserId(long userId);
}
