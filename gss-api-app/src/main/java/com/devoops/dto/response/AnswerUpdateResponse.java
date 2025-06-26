package com.devoops.dto.response;

import com.devoops.domain.entity.github.Answer;
import jakarta.validation.constraints.NotBlank;

public record AnswerUpdateResponse(
        long id,
        @NotBlank String content
) {

    public AnswerUpdateResponse(Answer answer) {
        this(answer.getId(), answer.getContent());
    }
}
