package com.devoops.dto.request;

import jakarta.validation.Valid;
import java.util.List;

public record AnswerPutRequests(
        @Valid List<AnswerPutRequest> answers
) {

}
