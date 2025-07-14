package com.devoops.command.request;

import com.devoops.domain.entity.github.Question;

public record QuestionCreateCommand(
    Long pullRequestId,
    String category,
    String content
) {
    public Question toDomainEntity() {
        return new Question(pullRequestId, category, content);
    }

}
