package com.devoops.exception;

import com.devoops.exception.errorcode.ErrorCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ErrorResponse(
        @NotNull String code,
        @NotNull String status,
        @NotNull String message
) {

    public ErrorResponse(ErrorCode errorCode) {
        this(
                errorCode.name(),
                errorCode.getStatus().name(),
                errorCode.getMessage()
        );
    }
}
