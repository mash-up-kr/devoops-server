package com.devoops.exception.errorcode;

import lombok.Getter;

@Getter
public enum ErrorCode {

    //4XX
    REGISTRY_GITHUB_REPOSITORY_NOT_FOUND(400, "입력한 깃허브 레포지토리를 등록할 수 없습니다"),
    REPOSITORY_NOT_FOUND(400, "레포지토리를 찾을 수 없습니다"),
    MALFORMED_GITHUB_REPOSITORY_URL(400, "잘못된 형식의 레포지토리 url입니다"),
    UNAUTHORIZED_EXCEPTION(401, "잘못된 유저 접근입니다"),
    INVALID_REFRESH_TOKEN(400, "잘못된 리프레시 토큰입니다"),
    TOKEN_TYPE_MISMATCH(400, "토큰 타입이 일치하지 않습니다"),
    EXPIRED_REFRESH_TOKEN(400, "만료된 리프레시 토큰입니다"),
    ALREADY_SAVED_REPOSITORY(400, "이미 저장된 레포지토리입니다"),

    FIELD_ERROR(400, "입력이 잘못되었습니다."),
    URL_PARAMETER_ERROR(400, "입력이 잘못되었습니다."),
    METHOD_ARGUMENT_TYPE_MISMATCH(400, "입력한 값의 타입이 잘못되었습니다."),
    NO_RESOURCE_FOUND(404, "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_SUPPORTED(405, "허용되지 않은 메서드입니다."),
    MEDIA_TYPE_NOT_SUPPORTED(415, "허용되지 않은 미디어 타입입니다."),
    ALREADY_DISCONNECTED(400, "이미 클라이언트에서 요청이 종료되었습니다."),
    TOKEN_EXPIRED(401, "인증 토큰이 만료되었습니다."),

    //5XX
    INTERNAL_SERVER_ERROR(500, "서버 오류가 발생했습니다. 관리자에게 문의하세요."),
    GITHUB_REPOSITORY_NOT_FOUND(500, "찾는 레포지토리가 존재하지 않습니다"),
    GITHUB_REPOSITORY_REGISTRY_COUNT_NOT_FOUND(500, "찾는 레포지토리 등록 카운트가 존재하지 않습니다"),
    INVALID_GITHUB_REPOSITORY_REGISTRY_COUNT(500, "레포지토리 등록 카운트가 0이하입니다"),
    GITHUB_TOKEN_NOT_FOUND(500, "찾는 깃허브 토큰이 존재하지 않습니다"),
    PULL_REQUEST_NOT_FOUND(500, "찾는 풀 리퀘스트가 존재하지 않습니다"),
    QUESTION_NOT_FOUND(500, "찾는 질문이 존재하지 않습니다"),
    ANSWER_NOT_FOUND(500, "찾는 대답이 존재하지 않습니다"),
    WEBHOOK_NOT_FOUND(500, "찾는 웹훅이 존재하지 않습니다"),
    ANSWER_RANKING_NOT_FOUND(500, "찾는 질문 랭킹이 존재하지 않습니다"),
    USER_NOT_FOUND(500, "찾는 회원이 존재하지 않습니다"),
    REDIS_PUBLISH_ERROR(500, "레디스 이벤트 발행 과정에서 문제가 생겼습니다"),
    REDIS_SUBSCRIBE_ERROR(500, "레디스 이벤트 수신 과정에서 문제가 생겼습니다"),
    GITHUB_CLIENT_ERROR(500, "깃허브 클라이언트 소통과정에 문제가 발생했습니다"),
    AI_RESPONSE_PARSING_ERROR(500, "AI로부터 온 질문 생성을 파싱하는 과정에 오류가 발생했습니다"),
    AI_CREATE_QUESTION_ERROR(500, "AI 질문 생성과정에 오류가 발생했습니다"),
    AI_CHARGE_NOT_FOUND(500, "당월 AI 비용을 찾을 없습니다.")
    ;

    private final int statusCode;
    private final String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
