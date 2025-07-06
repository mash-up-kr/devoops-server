package com.devoops.command.request;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.RecordStatus;

import java.time.LocalDateTime;

public record PullRequestCreateCommand(
        long repositoryId,
        long userId,
        String title,
        String description,
        long externalId,
        LocalDateTime mergedAt,
        String tag
) {

    public PullRequest toDomainEntity() {
        return new PullRequest(
                null,
                repositoryId,
                userId,
                title,
                description,
                null,
                externalId,
                RecordStatus.PENDING,
                mergedAt,
                tag
        );
    }
}
