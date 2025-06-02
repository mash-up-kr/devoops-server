package com.devoops.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserSaveRequest(
        @NotBlank String code,
        @NotBlank String redirectUrl
) {

}
