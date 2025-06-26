package com.devoops.dto.response;

import java.util.List;

public record RepositoryPullRequestResponses(
        List<RepositoryPullRequestResponse> pullRequests
) {

}
