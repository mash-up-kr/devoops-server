package com.devoops.controller.docs;

import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.PullRequestReadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Pull Request API")
@SecurityRequirement(name = "Authorization")
public interface PullRequestControllerSwagger {

    @Operation(
            summary = "PR 세부 내역 조회",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "PR 회고 종료 성공",
                    content = @Content(schema = @Schema(implementation = PullRequestReadResponse.class)))}
    )
    ResponseEntity<PullRequestReadResponse> getPullRequest(
            @Parameter(hidden = true) User user,
            long pullRequestId
    );

    @Operation(
            summary = "PR 회고 종료",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "PR 회고 종료 성공"
            )}
    )
    ResponseEntity<Void> pullRequestUpdateToDone(
            @Parameter(hidden = true) User user,
            long pullRequestId
    );
}
