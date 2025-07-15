package com.devoops.exception;

import com.devoops.exception.errorcode.ErrorCode;

public record ErrorResponse(
        String code,
        String status,
        String message
) {

    public ErrorResponse(ErrorCode errorCode) {
        this(
                errorCode.name(),
                errorCode.getStatus().name(),
                errorCode.getMessage()
        );
    }
}
