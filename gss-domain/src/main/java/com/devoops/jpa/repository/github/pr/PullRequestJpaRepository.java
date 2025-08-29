package com.devoops.jpa.repository.github.pr;

import com.devoops.domain.entity.github.pr.ProcessingStatus;
import com.devoops.jpa.entity.github.pr.PullRequestEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestJpaRepository extends JpaRepository<PullRequestEntity, Long> {

    Page<PullRequestEntity> findByRepositoryIdAndProcessingStatus(long repositoryId, ProcessingStatus processingStatus, Pageable pageable);

    Page<PullRequestEntity> findByUserIdAndProcessingStatus(long userId, ProcessingStatus status, Pageable pageable);

    Optional<PullRequestEntity> findById(long id);
}
