package com.devoops.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserSaveRequest(
        @NotBlank
        @Schema(description = "깃허브 액세스 토큰", example = "64a6f1dd4379e7abffa6")
        String githubAccessToken
) {

}
