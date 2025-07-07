package com.devoops.domain.entity.github;

import java.time.LocalDateTime;
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
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
