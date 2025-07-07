package com.devoops.dto.response;

import com.devoops.domain.entity.github.AnswerRankings;
import java.util.List;

public record PullRequestRankingResponses(
        List<PullRequestRankingResponse> answerRanking
) {

    public static PullRequestRankingResponses from(AnswerRankings answerRankings) {
        List<PullRequestRankingResponse> responses = answerRankings.getRankings()
                .stream()
                .map(PullRequestRankingResponse::new)
                .toList();
        return new PullRequestRankingResponses(responses);
    }
}
