package com.devoops.exception;

public class GithubForbiddenException extends RuntimeException {

    public GithubForbiddenException(String message) {
        super(message);
    }
}
