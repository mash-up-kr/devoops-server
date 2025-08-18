package com.devoops.domain.entity.github.pr;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PullRequest {

    private final Long id;
    private final long repositoryId;
    private final long userId;
    private final String title;
    private final String description;
    private final String summary;
    private final String summaryDetail;
    private final String pullRequestUrl;
    private final long externalId;
    private final RecordStatus recordStatus;
    private final LocalDateTime mergedAt;
    private final String tag;

    public boolean isPending() {
        return recordStatus == RecordStatus.PENDING;
    }
}
