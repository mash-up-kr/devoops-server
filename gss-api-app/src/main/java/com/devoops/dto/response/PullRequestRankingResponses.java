package com.devoops.dto.response;

import com.devoops.domain.entity.github.AnswerRankings;
import com.devoops.dto.request.AnswerPutRequest;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PullRequestRankingResponses(

        @ArraySchema(schema = @Schema(description = "회고 최신화 요청 목록", implementation = PullRequestRankingResponse.class))
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
