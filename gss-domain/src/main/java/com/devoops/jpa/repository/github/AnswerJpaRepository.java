package com.devoops.jpa.repository.github;

import com.devoops.jpa.entity.github.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {

    @Query("""
            select count(ae)
            from AnswerEntity ae
                        where ae.questionId in (
                                    select qe.id
                                    from QuestionEntity qe
                                    where qe.pullRequestId = :pullRequestId
                              )
            """)
    long getAnswerCountByPullRequestId(@Param(value = "pullRequestId") long pullRequestId);
}
