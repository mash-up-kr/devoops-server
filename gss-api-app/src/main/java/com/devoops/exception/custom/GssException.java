package com.devoops.exception.custom;

import com.devoops.exception.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class GssException extends RuntimeException {

    private final ErrorCode errorCode;

    public GssException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
