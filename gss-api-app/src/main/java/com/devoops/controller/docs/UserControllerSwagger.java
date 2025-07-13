package com.devoops.controller.docs;

import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.UserReadResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "User API")
public interface UserControllerSwagger {

    @Operation(
            summary = "유저 정보 조회",
            responses = {@ApiResponse(
                    responseCode = "200",
                    description = "유저 정보 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserReadResponse.class)))
            }
    )
    ResponseEntity<UserReadResponse> getUserInfo(@Parameter(hidden = true) User user);
}
