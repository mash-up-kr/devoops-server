package com.devoops.dto.response;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record QuestionResponse(
        long questionId,
        String category,
        String content,
        boolean isSelected,
        @Nullable Long answerId,
        @NotNull String answer

) {

}
