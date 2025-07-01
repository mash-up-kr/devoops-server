package com.devoops.dto.response;

import java.util.List;

public record PullRequestRankingResponses(
        List<PullRequestRankingResponse> answerRanking
) {

}
