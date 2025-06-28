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
        @NotNull List<QuestionResponse> questions
) {

    public static PullRequestDetailReadResponse from(PullRequest pullRequest, List<QuestionAnswer> prQuestions) {
        List<QuestionResponse> questionResponses = prQuestions.stream()
                .map(QuestionResponse::new)
                .toList();
        return new PullRequestDetailReadResponse(
                pullRequest.getId(),
                pullRequest.getTitle(),
                pullRequest.getTag(),
                pullRequest.getRecordStatus(),
                pullRequest.getMergedAt(),
                pullRequest.getSummary(),
                resolveUniqueCategories(prQuestions),
                questionResponses
        );
    }

    private static List<String> resolveUniqueCategories(List<QuestionAnswer> questions) {
        return questions.stream()
                .map(QuestionAnswer::getCategory)
                .distinct()
                .toList();
    }
}
