package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.PullRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestJpaRepository extends JpaRepository<PullRequestEntity, String> {

}
