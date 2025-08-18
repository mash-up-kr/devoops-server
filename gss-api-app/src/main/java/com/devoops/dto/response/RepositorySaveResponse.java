package com.devoops.dto.response;

import com.devoops.domain.entity.github.repo.GithubRepository;
import io.swagger.v3.oas.annotations.media.Schema;

public record RepositorySaveResponse(
        @Schema(description = "레포지토리 id", example = "1")
        long id,

        @Schema(description = "레포지토리 소유 유저 id", example = "2")
        long ownerId,

        @Schema(description = "레포지토리 이름", example = "dev-oops")
        String name,

        @Schema(description = "레포지토리 url", example = "https://github.com/devoops/devoops-api")
        String url
) {

        public RepositorySaveResponse(GithubRepository repository) {
                this(repository.getId(), repository.getUserId(), repository.getName(), repository.getUrl());
        }
}
