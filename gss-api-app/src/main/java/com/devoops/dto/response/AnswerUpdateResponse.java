package com.devoops.dto.response;

import com.devoops.domain.entity.github.Answer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AnswerUpdateResponse(

        @Schema(description = "회고 id", example = "1")
        long id,

        @NotBlank
        @Schema(description = "회고 내용", example = "엄청난 깨달음!")
        String content
) {

    public AnswerUpdateResponse(Answer answer) {
        this(answer.getId(), answer.getContent());
    }
}
