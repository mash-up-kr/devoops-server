package com.devoops.domain.entity.github.answer;

import io.micrometer.common.util.StringUtils;
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

    public boolean isBlank() {
        return StringUtils.isBlank(content);
    }
}
