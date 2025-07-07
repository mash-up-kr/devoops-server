package com.devoops.dto.response;

import com.devoops.domain.entity.github.Answer;
import io.swagger.v3.oas.annotations.media.Schema;

public record AnswerSaveResponse(

        @Schema(description = "회고 id", example = "1")
        long id
) {

    public AnswerSaveResponse(Answer answer){
        this(answer.getId());
    }
}
