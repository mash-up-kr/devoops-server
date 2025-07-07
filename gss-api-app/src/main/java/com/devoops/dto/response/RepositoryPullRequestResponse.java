package com.devoops.dto.response;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.RecordStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RepositoryPullRequestResponse(
        @Schema(description = "풀 리퀘스트 ID", example = "1")
        long id,

        @NotBlank
        @Schema(description = "풀 리퀘스트 제목", example = "[FIX] 서버 장애 대응")
        String title,

        @NotNull
        @Schema(description = "기록 상태", example = "PROGRESS")
        RecordStatus recordStatus,

        @NotNull
        @Schema(description = "머지된 날짜", example = "2025-07-07")
        LocalDate mergedAt,

        @NotBlank
        @Schema(description = "회고 요약", example = "이번 장애에서의 문제 상황과 대응 과정을 정리하였습니다.")
        String summary,

        @Schema(description = "PR 라벨", example = "feat")
        String tag
) {

    public RepositoryPullRequestResponse(PullRequest pullRequest) {
        this(
                pullRequest.getId(),
                pullRequest.getTitle(),
                pullRequest.getRecordStatus(),
                pullRequest.getMergedAt().toLocalDate(),
                pullRequest.getSummary(),
                pullRequest.getTag()
        );
    }
}
