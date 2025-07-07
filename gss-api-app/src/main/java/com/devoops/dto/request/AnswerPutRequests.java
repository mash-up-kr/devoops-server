package com.devoops.dto.request;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.util.List;

public record AnswerPutRequests(

        @ArraySchema(schema = @Schema(description = "회고 최신화 요청 목록", implementation = AnswerPutRequest.class))
        @Valid
        List<AnswerPutRequest> answers
) {

}
