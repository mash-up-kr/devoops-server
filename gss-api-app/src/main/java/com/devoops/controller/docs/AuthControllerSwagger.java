package com.devoops.controller.docs;

import com.devoops.dto.request.UserSaveRequest;
import com.devoops.dto.response.UserSaveResponse;
import com.devoops.dto.response.UserTokenRefreshResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth API")
public interface AuthControllerSwagger {

    @Operation(
            summary = "회원 생성 & 토큰 발급",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = UserSaveRequest.class))),
            responses = {@ApiResponse(
                    responseCode = "201",
                    description = "회원 생성 & 토큰 발급 성공",
                    content = @Content(schema = @Schema(implementation = UserSaveResponse.class)))}
    )
    ResponseEntity<UserSaveResponse> issueToken(UserSaveRequest userSaveRequest);

    @Operation(
            summary = "토큰 재발급",
            parameters = {
                    @Parameter(
                            name = "refreshToken",
                            description = "리프레시 토큰 (쿠키에 담김)",
                            in = ParameterIn.COOKIE,
                            required = true,
                            example = "refreshToken=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                    )
            },
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "토큰 재발급 성공",
                    content = @Content(schema = @Schema(implementation = UserTokenRefreshResponse.class)))}
    )
    ResponseEntity<UserTokenRefreshResponse> reIssueToken(String token);
}
