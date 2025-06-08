package com.devoops.domain.entity.github;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PullRequest {

    private final long repositoryId;
    private final long userId;
    private final String title;
    private final String description;
    private final String summary;
    private final long externalId;
    private final RecordStatus recordStatus;
    private final LocalDateTime mergedAt;
}
