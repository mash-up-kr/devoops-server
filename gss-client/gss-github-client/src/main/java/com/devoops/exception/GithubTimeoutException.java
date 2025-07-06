package com.devoops.exception;

public class GithubTimeoutException extends RuntimeException {
    public GithubTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
