package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.jpa.entity.github.QuestionEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Integer> {

    Optional<QuestionEntity> findById(long id);

    @Query("""
            select new com.devoops.domain.entity.github.QuestionAnswer(
                        qe.id,
                        qe.category,
                        qe.content,
                        qe.isAnswered,
                        ae.id,
                        ae.content,
                        ae.createdAt,
                        ae.updatedAt
              )
              from QuestionEntity qe
              left outer join AnswerEntity ae on qe.id = ae.questionId
              where qe.pullRequestId = :pullRequestId
            """)
    List<QuestionAnswer> findByPullRequestId(@Param("pullRequestId") long pullRequestId);
}
