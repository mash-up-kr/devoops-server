package com.devoops.jpa.entity.github;

import com.devoops.domain.entity.github.AnswerRanking;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Column(name = "content")
    private String questionContent;

    @Column(name = "pull_request_id")
    private long pullRequestId;

    @Column(name = "pull_request_title")
    private String pullRequestTitle;

    @Column(name = "user_id")
    private long userId;

    private LocalDateTime updatedAt;

    public AnswerRankingEntity(AnswerRanking answerRanking) {
        this(
                null,
                answerRanking.getQuestionId(),
                answerRanking.getQuestionContent(),
                answerRanking.getPullRequestId(),
                answerRanking.getPullRequestTitle(),
                answerRanking.getUserId(),
                answerRanking.getUpdatedAt()
        );
    }

    public AnswerRanking toDomainEntity() {
        return new AnswerRanking(id, pullRequestId, pullRequestTitle, userId, questionId, questionContent, updatedAt);
    }

    public void update(AnswerRanking answerRanking) {
        this.questionId = answerRanking.getQuestionId();
        this.questionContent = answerRanking.getQuestionContent();
        this.pullRequestTitle = answerRanking.getPullRequestTitle();
        this.pullRequestId = answerRanking.getPullRequestId();
        this.userId = answerRanking.getUserId();
        this.updatedAt = answerRanking.getUpdatedAt();
    }
}
