package com.devoops.dto.response;

import com.devoops.domain.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserSaveResponse(

        @Schema(description = "회원 이메일", example = "example@email.com")
        String email
) {

    public UserSaveResponse(User user){
        this(user.getEmail());
    }
}
