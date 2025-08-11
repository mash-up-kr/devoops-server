package com.devoops.jpa.repository.github.answer;

import com.devoops.jpa.entity.github.answer.AnswerRankingEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRankingJpaRepository extends JpaRepository<AnswerRankingEntity, Long> {

    List<AnswerRankingEntity> findAllByUserId(long userId);

    Optional<AnswerRankingEntity> findByPullRequestId(long pullRequestId);

    void deleteByPullRequestIdIn(List<Long> pullRequestIds);
}
