package com.devoops.domain.entity.github;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Answer {

    private final long questionId;
    private final String content;
}
