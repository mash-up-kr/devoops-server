package com.devoops.dto.response;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.entity.github.RecordStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record PullRequestReadResponse(
        long id,
        @NotBlank String title,
        String tag,
        @NotNull RecordStatus recordStatus,
        @NotNull LocalDateTime mergedAt,
        @NotBlank String summary,
        List<String> categories,
        List<QuestionResponse> questions
) {

    public static PullRequestReadResponse from(
            PullRequest pullRequest,
            List<String> categories,
            List<QuestionAnswer> questionAnswers
    ) {
        List<QuestionResponse> questionResponses = mapToQuestionResponses(questionAnswers);
        return new PullRequestReadResponse(
                pullRequest.getId(),
                pullRequest.getTitle(),
                pullRequest.getTag(),
                pullRequest.getRecordStatus(),
                pullRequest.getMergedAt(),
                pullRequest.getSummary(),
                categories,
                questionResponses
        );
    }

    private static List<QuestionResponse> mapToQuestionResponses(List<QuestionAnswer> questionAnswers) {
        return questionAnswers.stream()
                .map(QuestionResponse::new)
                .toList();
    }
}
