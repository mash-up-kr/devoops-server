package com.devoops.controller.docs;

import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.AnswerPutRequests;
import com.devoops.dto.request.AnswerUpdateRequest;
import com.devoops.dto.response.AnswerPutResponses;
import com.devoops.dto.response.AnswerSaveResponse;
import com.devoops.dto.response.AnswerUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface QuestionControllerSwagger {

    @Operation(
            summary = "회고 생성",
            responses = {@ApiResponse(
                    responseCode = "201",
                    description = "회고 생성 성공",
                    content = @Content(schema = @Schema(implementation = AnswerSaveResponse.class)))
            }
    )
    ResponseEntity<AnswerSaveResponse> createAnswer(
            @Parameter(hidden = true) User user,
            long questionId
    );

    @Operation(
            summary = "다수 회고 최신화",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AnswerPutRequests.class))),
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "다수 회고 최신화 성공",
                    content = @Content(schema = @Schema(implementation = AnswerSaveResponse.class)))
            }
    )
    ResponseEntity<AnswerPutResponses> updateAllAnswer(
            @Parameter(hidden = true) User user,
            AnswerPutRequests request
    );

    @Operation(
            summary = "단일 회고 갱신",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AnswerUpdateRequest.class))),
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "단일 회고 갱신 성공",
                    content = @Content(schema = @Schema(implementation = AnswerUpdateResponse.class)))
            }
    )
    ResponseEntity<AnswerUpdateResponse> updateAnswer(
            @Parameter(hidden = true) User user,
            long answerId,
            AnswerUpdateRequest request
    );

    @Operation(
            summary = "회고 삭제",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = AnswerUpdateRequest.class))),
            responses = {@ApiResponse(
                    responseCode = "204",
                    description = "단일 회고 삭제 성공"
            )}
    )
    ResponseEntity<Void> deleteAnswer(
            @Parameter(hidden = true) User user,
            long answerId
    );
}
