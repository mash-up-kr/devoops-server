package com.devoops.event;

import java.time.LocalDateTime;

public record AnalyzePrEvent(
        Boolean isMerged,
        long pullRequestId,
        String diffUrl,
        String title,
        String description,
        String label,
        long repositoryId,
        long userId,
        LocalDateTime mergedAt
) {

}
