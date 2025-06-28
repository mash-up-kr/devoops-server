package com.devoops.dto.response;

import com.devoops.domain.entity.github.RecordStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record PullRequestReadResponse(
        long id,
        @NotNull String title,
        @NotNull String tag,
        @NotNull RecordStatus recordStatus,
        @NotNull LocalDateTime mergedAt,
        @NotBlank String summary,
        @NotNull List<String> categories,
        @NotNull List<QuestionResponse> questions
) {

}
