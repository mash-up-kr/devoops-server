package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.jpa.entity.github.PullRequestEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PullRequestJpaRepository extends JpaRepository<PullRequestEntity, Long> {

    Page<PullRequestEntity> findByRepositoryId(long repositoryId, Pageable pageable);

    Page<PullRequestEntity> findByUserId(long userId, Pageable pageable);

    Optional<PullRequestEntity> findById(long id);
}
