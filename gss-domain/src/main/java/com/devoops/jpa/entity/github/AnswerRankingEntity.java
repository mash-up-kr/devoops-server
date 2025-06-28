package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.AnswerRanking;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerRankingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id")
    private long questionId;

    @Column(name = "pull_request_id")
    private long pullRequestId;

    @Column(name = "user_id")
    private long userId;

    private LocalDateTime updatedAt;

    public AnswerRankingEntity(AnswerRanking answerRanking) {
        this(
                null,
                answerRanking.getQuestionId(),
                answerRanking.getPullRequestId(),
                answerRanking.getUserId(),
                answerRanking.getUpdatedAt()
        );
    }

    public AnswerRanking toDomainEntity() {
        return new AnswerRanking(id, questionId, pullRequestId, userId, updatedAt);
    }

    public void update(AnswerRanking answerRanking) {
        this.questionId = answerRanking.getQuestionId();
        this.pullRequestId = answerRanking.getPullRequestId();
        this.userId = answerRanking.getUserId();
        this.updatedAt = answerRanking.getUpdatedAt();
    }
}
