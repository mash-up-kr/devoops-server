package com.devoops.exception;

import com.devoops.exception.errorcode.ErrorCode;
import jakarta.validation.constraints.NotBlank;

public record ErrorResponse(
        @NotBlank String code,
        @NotBlank String status,
        @NotBlank String message
) {

    public ErrorResponse(ErrorCode errorCode) {
        this(
                errorCode.name(),
                errorCode.getStatus().name(),
                errorCode.getMessage()
        );
    }
}
