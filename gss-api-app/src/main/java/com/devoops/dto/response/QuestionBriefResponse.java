package com.devoops.dto.response;

import com.devoops.domain.entity.github.QuestionAnswer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record QuestionBriefResponse(
        @Schema(description = "질문 id", example = "1")
        long id,

        @Schema(description = "질문 선택 유무", example = "true")
        boolean isSelected,

        @Schema(description = "카테고리", example = "성능")
        String category,

        @Schema(description = "질문 내용", example = "성능적으로 좋은 선택이라 생각하나요?")
        String content,

        @Schema(description = "최초 대답한 시간", example = "2025-06-24T15:29:45Z")
        LocalDateTime createdAt,

        @Schema(description = "가장 최근 대답한 시간", example = "2025-06-24T15:29:45Z")
        LocalDateTime updatedAt
) {

        public QuestionBriefResponse(QuestionAnswer questionAnswer) {
                this(
                        questionAnswer.getQuestionId(),
                        questionAnswer.isSelected(),
                        questionAnswer.getCategory(),
                        questionAnswer.getContent(),
                        questionAnswer.getCreatedAt(),
                        questionAnswer.getUpdatedAt()
                );
        }

}
