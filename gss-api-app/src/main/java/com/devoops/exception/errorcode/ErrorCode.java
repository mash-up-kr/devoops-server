package com.devoops.exception.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //4XX
    REPOSITORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "레포지토리를 찾을 수 없습니다"),
    MALFORMED_GITHUB_REPOSITORY_URL(HttpStatus.BAD_REQUEST, "잘못된 형식의 레포지토리 url입니다"),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "잘못된 유저 접근입니다"),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "잘못된 리프레시 토큰입니다"),
    TOKEN_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "토큰 타입이 일치하지 않습니다"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "만료된 리프레시 토큰입니다"),
    ALREADY_SAVED_REPOSITORY(HttpStatus.BAD_REQUEST, "이미 저장된 레포지토리입니다"),

    FIELD_ERROR(HttpStatus.BAD_REQUEST, "입력이 잘못되었습니다."),
    URL_PARAMETER_ERROR(HttpStatus.BAD_REQUEST, "입력이 잘못되었습니다."),
    METHOD_ARGUMENT_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "입력한 값의 타입이 잘못되었습니다."),
    NO_RESOURCE_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_SUPPORTED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "허용되지 않은 미디어 타입입니다."),
    ALREADY_DISCONNECTED(HttpStatus.BAD_REQUEST, "이미 클라이언트에서 요청이 종료되었습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다."),

    //5XX
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다. 관리자에게 문의하세요."),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
