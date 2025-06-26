package com.devoops.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AnswerPutRequest (
        long answerId,
        @NotBlank String content
) {
}
