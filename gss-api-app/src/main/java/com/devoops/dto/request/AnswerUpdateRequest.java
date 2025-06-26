package com.devoops.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AnswerUpdateRequest(
        @NotBlank String content
) {

}
