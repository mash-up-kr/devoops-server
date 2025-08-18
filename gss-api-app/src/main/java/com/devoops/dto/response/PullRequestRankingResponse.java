package com.devoops.dto.response;

import com.devoops.domain.entity.github.answer.AnswerRanking;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PullRequestRankingResponse(

        @Schema(description = "PR ID", example = "1")
        long pullRequestId,

        @NotBlank
        @Schema(description = "PR 제목", example = "성능 개선 회고")
        String title,

        @NotBlank
        @Schema(description = "질문 내용", example = "이 PR에서 성능 개선을 위해 어떤 기법을 적용했나요?")
        String content,

        @NotNull
        @Schema(description = "회고가 마지막으로 수정된 시각", example = "2025-07-10T14:23:11")
        LocalDateTime updatedAt
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
