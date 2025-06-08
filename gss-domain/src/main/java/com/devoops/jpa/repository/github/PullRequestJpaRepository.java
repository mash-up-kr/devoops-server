package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.PullRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestJpaRepository extends JpaRepository<PullRequest, String> {

}
