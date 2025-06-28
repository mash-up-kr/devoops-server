package com.devoops.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RepositorySaveRequest(
        @NotBlank String url
) {
}
