package com.devoops.exception;

import com.devoops.domain.notifier.ErrorNotifier;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorNotifier errorNotifier;

    @ExceptionHandler(GssException.class)
    public ResponseEntity<ErrorResponse> handleGssException(GssException exception) {
        log.error("Custom GssException occurred: {}", exception.getMessage(), exception);
        errorNotifier.notify(exception);
        return toResponse(exception.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        log.error("Unhandled exception occurred", exception);
        errorNotifier.notify(exception);
        return toResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> toResponse(ErrorCode errorCode) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode);
        return ResponseEntity.status(errorCode.getStatusCode())
                .body(errorResponse);
    }
}
