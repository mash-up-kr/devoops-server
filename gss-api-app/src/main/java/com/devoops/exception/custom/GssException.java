package com.devoops.exception.custom;

import com.devoops.exception.errorcode.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public class GssException extends RuntimeException {

    private final ErrorCode errorCode;
}
