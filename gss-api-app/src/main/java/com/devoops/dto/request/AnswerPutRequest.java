package com.devoops.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AnswerPutRequest(

        @Schema(description = "회고 id", example = "1")
        long answerId,

        @Schema(description = "회고 내용", example = "엄청난 깨달음!")
        @NotBlank
        String content
) {

}
