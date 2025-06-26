package com.devoops.dto.response;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.RecordStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RepositoryPullRequestResponse(
        long id,
        @NotBlank String title,
        @NotNull RecordStatus recordStatus,
        @NotNull LocalDate mergedAt,
        @NotBlank String summary,
        String tag
) {

    public RepositoryPullRequestResponse(PullRequest pullRequest) {
        this(
                pullRequest.getId(),
                pullRequest.getTitle(),
                pullRequest.getRecordStatus(),
                pullRequest.getMergedAt().toLocalDate(),
                pullRequest.getSummary(),
                pullRequest.getTag()
        );
    }

}
