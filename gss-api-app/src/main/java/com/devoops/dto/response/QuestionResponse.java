package com.devoops.dto.response;

import com.devoops.domain.entity.github.QuestionAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record QuestionResponse(

        @Schema(description = "질문 id", example = "1")
        long questionId,

        @Schema(description = "카테고리", example = "성능")
        String category,

        @Schema(description = "질문 내용", example = "성능적으로 좋은 선택이라 생각하나요?")
        String content,

        @Schema(description = "질문 선택 유무", example = "true")
        boolean isSelected,

        @Schema(description = "회고 id", example = "1")
        @Nullable
        Long answerId,

        @Schema(description = "회고 내용", example = "나의 소중한 답변")
        @NotNull
        String answer

) {

    public QuestionResponse(QuestionAnswer question) {
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
