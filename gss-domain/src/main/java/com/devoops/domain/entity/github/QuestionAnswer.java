package com.devoops.domain.entity.github;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class QuestionAnswer {

    private final long questionId;
    private final String category;
    private final String content;
    private final boolean isSelected;
    private final Long answerId;
    private final String answer;
}
