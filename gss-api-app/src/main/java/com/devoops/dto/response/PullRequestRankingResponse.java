package com.devoops.dto.response;

import com.devoops.domain.entity.github.AnswerRanking;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PullRequestRankingResponse(
        long pullRequestId,
        @NotBlank String title,
        @NotBlank String content,
        @NotNull LocalDateTime updatedAt
) {

    public PullRequestRankingResponse(AnswerRanking answerRanking) {
        this(
                answerRanking.getPullRequestId(),
                answerRanking.getPullRequestTitle(),
                answerRanking.getQuestionContent(),
                answerRanking.getUpdatedAt()
        );
    }
}
