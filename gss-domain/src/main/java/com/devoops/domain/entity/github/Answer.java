package com.devoops.domain.entity.github;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Answer {

    private static final String INITIALIZED_ANSWER_CONTENT = "";

    private final Long id;
    private final long questionId;
    private final String content;

    public static Answer initialize(long questionId) {
        return new Answer(null, questionId, INITIALIZED_ANSWER_CONTENT);
    }
}
