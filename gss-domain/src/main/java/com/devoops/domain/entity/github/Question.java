package com.devoops.domain.entity.github;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Question {

    private final Long id;
    private final long pullRequestId;
    private final String content;
    private final boolean isAnswered;
}
