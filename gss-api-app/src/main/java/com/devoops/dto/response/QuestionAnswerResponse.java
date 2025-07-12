package com.devoops.dto.response;

import com.devoops.domain.entity.github.QuestionAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record QuestionAnswerResponse(

        @Schema(description = "질문 ID", example = "101")
        long questionId,

        @Schema(description = "질문 카테고리", example = "가독성")
        String category,

        @Schema(description = "질문 내용", example = "이 코드에서 가장 가독성이 떨어지는 부분은 무엇인가요?")
        String content,

        @Schema(description = "선택된 질문 여부", example = "true")
        boolean isSelected,

        @Nullable
        @Schema(description = "답변 ID (없을 수도 있음)", example = "202", nullable = true)
        Long answerId,


        @Nullable
        @Schema(description = "답변 내용", example = "변수명이 추상적이라 처음 읽을 때 이해가 어려웠습니다.", nullable = true)
        String answer
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
