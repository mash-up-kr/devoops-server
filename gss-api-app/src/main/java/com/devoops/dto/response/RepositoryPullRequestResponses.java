package com.devoops.dto.response;

import com.devoops.domain.entity.github.pr.PullRequests;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.stream.Collectors;

public record RepositoryPullRequestResponses(

        @ArraySchema(schema = @Schema(description = "레포지토리 PR 리스트 모음", implementation = RepositoryPullRequestResponse.class))
        List<RepositoryPullRequestResponse> pullRequests
) {

    public static RepositoryPullRequestResponses from(PullRequests pullRequests) {
        return pullRequests.getValues().stream()
                .map(RepositoryPullRequestResponse::new)
                .collect(Collectors.collectingAndThen(Collectors.toList(), RepositoryPullRequestResponses::new));
    }
}
