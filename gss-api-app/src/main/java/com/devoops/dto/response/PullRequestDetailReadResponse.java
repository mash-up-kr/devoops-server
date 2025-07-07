package com.devoops.dto.response;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.entity.github.RecordStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record PullRequestDetailReadResponse(
        long id,
        @NotNull String title,
        @NotNull String tag,
        @NotNull RecordStatus recordStatus,
        @NotNull LocalDateTime mergedAt,
        @NotBlank String summary,
        @NotNull List<String> categories,
        @NotNull List<QuestionAnswerResponse> questions
) {

    public static PullRequestDetailReadResponse from(
            PullRequest pullRequest,
            List<String> categories,
            List<QuestionAnswer> prQuestions
    ) {
        List<QuestionAnswerResponse> questionAnswerRespons = prQuestions.stream()
                .map(QuestionAnswerResponse::new)
                .toList();
        return new PullRequestDetailReadResponse(
                pullRequest.getId(),
                pullRequest.getTitle(),
                pullRequest.getTag(),
                pullRequest.getRecordStatus(),
                pullRequest.getMergedAt(),
                pullRequest.getSummary(),
                categories,
                questionAnswerRespons
        );
    }
}
