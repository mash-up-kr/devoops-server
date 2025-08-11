package com.devoops.controller.docs;

import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.RepositorySaveRequest;
import com.devoops.dto.response.MyRepositoriesResponse;
import com.devoops.dto.response.RepositoryPullRequestResponses;
import com.devoops.dto.response.RepositorySaveResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Repository API")
@SecurityRequirement(name = "Authorization")
public interface RepositoryControllerSwagger {

    @Operation(
            summary = "레포지토리 PR 리스트 반환",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "레포지토리 PR 리스트 반환 성공",
                    content = @Content(schema = @Schema(implementation = RepositoryPullRequestResponses.class)))
            }
    )
    ResponseEntity<RepositoryPullRequestResponses> getRepositoryPullRequests(
            @Parameter(hidden = true) User user,
            @Parameter(
                    name = "repositoryId",
                    description = "레포지토리 ID",
                    example = "100"
            ) long repositoryId,

            @Parameter(
                    name = "size",
                    description = "페이지 사이즈 크기",
                    example = "5"
            )
            int size,
            @Parameter(
                    name = "page",
                    description = "페이지 숫자",
                    example = "10"
            )
            int page
    );

    @Operation(
            summary = "회원의 PR 리스트 반환",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "회원 소유의 PR 리스트 반환 성공",
                    content = @Content(schema = @Schema(implementation = RepositoryPullRequestResponses.class)))
            }
    )
    ResponseEntity<RepositoryPullRequestResponses> getRepositoryEntirePullRequests(
            @Parameter(hidden = true) User user,
            @Parameter(
                    name = "size",
                    description = "페이지 사이즈 크기",
                    example = "5"
            )
            int size,
            @Parameter(
                    name = "page",
                    description = "페이지 숫자",
                    example = "10"
            )
            int page
    );

    @Operation(
            summary = "신규 레포지토리 저장",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = RepositorySaveRequest.class))),
            responses = {@ApiResponse(
                    responseCode = "201",
                    description = "신규 레포지토리 생성 성공",
                    content = @Content(schema = @Schema(implementation = RepositorySaveResponse.class)))
            }
    )
    ResponseEntity<RepositorySaveResponse> saveRepository(
            @Parameter(hidden = true) User user,
            RepositorySaveRequest request
    );

    @Operation(
            summary = "나의 레포지토리 목록 조회",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "나의 레포지토리 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = MyRepositoriesResponse.class)))
            }
    )
    ResponseEntity<MyRepositoriesResponse> getMyRepositories(@Parameter(hidden = true) User user);

    @Operation(
            summary = "레포지토리 삭제",
            responses = {@ApiResponse(
                    responseCode = "204",
                    description = "나의 레포지토리 삭제 성공")}
    )
    ResponseEntity<Void> deleteRepositories(
            @Parameter(hidden = true) User user,
            long repositoryId
    );
}
