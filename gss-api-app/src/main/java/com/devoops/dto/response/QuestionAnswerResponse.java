package com.devoops.dto.response;

import com.devoops.domain.entity.github.QuestionAnswer;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record QuestionAnswerResponse(
        long questionId,
        String category,
        String content,
        boolean isSelected,
        @Nullable Long answerId,
        @NotNull String answer

) {

    public QuestionAnswerResponse(QuestionAnswer question) {
        this(
                question.getQuestionId(),
                question.getCategory(),
                question.getContent(),
                question.isSelected(),
                question.getAnswerId(),
                question.getAnswer()
        );
    }
}
