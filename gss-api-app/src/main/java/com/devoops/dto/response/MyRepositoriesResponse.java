package com.devoops.dto.response;

import com.devoops.domain.entity.github.repo.GithubRepository;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MyRepositoriesResponse(
        @ArraySchema(schema = @Schema(description = "레포지토리 내역", implementation = RepositorySummary.class))
        List<RepositorySummary> repositories
) {
    public static MyRepositoriesResponse from(List<GithubRepository> repositories) {
        return new MyRepositoriesResponse(
            repositories.stream()
                .map(RepositorySummary::from)
                .toList()
        );
    }

    public record RepositorySummary(
            @Schema(description = "레포지토리 아이디", example = "1")
            long id,

            @Schema(description = "레포지토리 이름", example = "my-repo")
            String name,

            @Schema(description = "레포지토리 추적 여부", example = "true")
            boolean isTracking,

            @Schema(description = "레포지토리 풀 리퀘스트 개수", example = "10")
            int pullRequestCount
    ) {
        public static RepositorySummary from(GithubRepository repository) {
            return new RepositorySummary(
                repository.getId(),
                repository.getName(),
                repository.isTracking(),
                repository.getPrCount()
            );
        }
    }
}
