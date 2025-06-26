package com.devoops.dto.response;

import com.devoops.domain.entity.github.Answer;

public record AnswerSaveResponse(
        long id
) {

    public AnswerSaveResponse(Answer answer){
        this(answer.getId());
    }
}
