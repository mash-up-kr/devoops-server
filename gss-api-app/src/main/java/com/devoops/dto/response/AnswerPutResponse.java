package com.devoops.dto.response;

import com.devoops.domain.entity.github.Answer;

public record AnswerPutResponse(
        long answerId,
        String content
) {

    public AnswerPutResponse(Answer answer) {
        this(answer.getId(), answer.getContent());
    }
}
