package com.devoops.dto.response;

import com.devoops.domain.entity.github.PullRequests;
import java.util.List;
import java.util.stream.Collectors;

public record RepositoryPullRequestResponses(
        List<RepositoryPullRequestResponse> pullRequests
) {

    public static RepositoryPullRequestResponses from(PullRequests pullRequests) {
        return pullRequests.getValues().stream()
                .map(RepositoryPullRequestResponse::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), RepositoryPullRequestResponses::new));
    }
}
