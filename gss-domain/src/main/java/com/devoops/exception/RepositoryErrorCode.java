package com.devoops.exception;

import lombok.Getter;

@Getter
public enum RepositoryErrorCode {

    GITHUB_REPOSITORY_NOT_FOUND("찾는 레포지토리가 존재하지 않습니다"),
    QUESTION_NOT_FOUND("찾는 질문이 존재하지 않습니다"),
    ;

    private final String message;

    RepositoryErrorCode(String message) {
        this.message = message;
    }
}
