package com.devoops.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record PullRequestRankingResponse(
        long pullRequestId,
        @NotBlank String title,
        @NotBlank String content,
        @NotNull LocalDateTime updatedAt
) {

}
