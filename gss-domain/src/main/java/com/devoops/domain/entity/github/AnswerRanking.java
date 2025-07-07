package com.devoops.domain.entity.github;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AnswerRanking {

    private final Long id;
    private final long pullRequestId;
    private final String pullRequestTitle;
    private final long userId;
    private final long questionId;
    private final String questionContent;
    private final LocalDateTime updatedAt;
}
