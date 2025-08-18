package com.devoops.dto.response;

import com.devoops.domain.entity.github.answer.Answers;
import java.util.List;
import java.util.stream.Collectors;

public record AnswerPutResponses(
        List<AnswerPutResponse> answers
) {

    public static AnswerPutResponses from(Answers answers) {
        return answers.getValues().stream()
                .map(AnswerPutResponse::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), AnswerPutResponses::new));
    }
}
