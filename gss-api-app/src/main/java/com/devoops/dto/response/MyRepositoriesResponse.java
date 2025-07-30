package com.devoops.dto.response;

import com.devoops.domain.entity.github.GithubRepository;

import java.util.List;

public record MyRepositoriesResponse(
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
        long id,
        String name,
        boolean isTracking,
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
