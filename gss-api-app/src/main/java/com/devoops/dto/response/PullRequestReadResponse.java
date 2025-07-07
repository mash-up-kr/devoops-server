package com.devoops.dto.response;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.entity.github.RecordStatus;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record PullRequestReadResponse(
        @Schema(description = "풀 리퀘스트 id", example = "1")
        long id,

        @NotNull
        @Schema(description = "회고 제목", example = "서비스 장애 회고")
        String title,

        @NotNull
        @Schema(description = "회고 라밸", example = "feat")
        String tag,

        @NotNull
        @Schema(description = "기록 상태", example = "PROGRESS")
        RecordStatus recordStatus,

        @NotNull
        @Schema(description = "머지 시각", example = "2025-07-07T13:45:30")
        LocalDateTime mergedAt,

        @NotBlank
        @Schema(description = "PR 2줄 요약", example = "이 PR은 ~~를 위해 만들어진 PR입니다")
        String summary,

        @NotNull
        @Schema(description = "회고 카테고리 목록", example = "{성능, 안전성, 실용성, 가독성}")
        List<String> categories,

        @NotNull
        @ArraySchema(schema = @Schema(description = "회고-질문 내역 목록", implementation = QuestionBriefResponse.class))
        List<QuestionBriefResponse> questions
) {

    public static PullRequestReadResponse from(
            PullRequest pullRequest,
            List<String> categories,
            List<QuestionAnswer> questionAnswers
    ) {
        return new PullRequestReadResponse(
                pullRequest.getId(),
                pullRequest.getTitle(),
                pullRequest.getTag(),
                pullRequest.getRecordStatus(),
                pullRequest.getMergedAt(),
                pullRequest.getSummary(),
                categories,
                mapToQuestionResponses(questionAnswers)
        );
    }

    private static List<QuestionBriefResponse> mapToQuestionResponses(List<QuestionAnswer> questionAnswers) {
        return questionAnswers.stream()
                .map(QuestionBriefResponse::new)
                .toList();
    }
}
