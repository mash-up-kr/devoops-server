package com.devoops.exception;

import com.devoops.exception.errorcode.ErrorCode;
import jakarta.annotation.Nonnull;

public record ErrorResponse(
        @Nonnull String code,
        @Nonnull String status,
        @Nonnull String message
) {

    public ErrorResponse(ErrorCode errorCode) {
        this(
                errorCode.name(),
                errorCode.getStatus().name(),
                errorCode.getMessage()
        );
    }
}
