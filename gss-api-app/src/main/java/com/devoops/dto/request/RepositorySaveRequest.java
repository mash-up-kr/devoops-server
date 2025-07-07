package com.devoops.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record RepositorySaveRequest(
        @NotBlank
        @Schema(description = "레포지토리 URL", example = "https://github.com/gss/coli")
        String url
) {

}
