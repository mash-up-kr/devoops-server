package com.devoops.exception;

import com.devoops.exception.errorcode.ErrorCode;
import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String code,
        String status,
        String message
) {

    public ErrorResponse(ErrorCode errorCode) {
        this(
                errorCode.name(),
                HttpStatus.valueOf(errorCode.getStatusCode()).name(),
                errorCode.getMessage()
        );
    }
}
