package com.devoops.dto.response;

import com.devoops.domain.entity.github.QuestionAnswer;
import java.time.LocalDateTime;

public record QuestionResponse(
        long id,
        boolean isSelected,
        String category,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public QuestionResponse(QuestionAnswer questionAnswer) {
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
