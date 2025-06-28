package com.devoops.jpa.entity.github;

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

    public AnswerRankingEntity(long questionId, long pullRequestId, long userId, LocalDateTime updatedAt) {
        this(null, questionId, pullRequestId, userId, updatedAt);
    }
}
