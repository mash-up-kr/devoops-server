package com.devoops.exception;

public class GssRepositoryException extends RuntimeException {
    public GssRepositoryException(RepositoryErrorCode repositoryErrorCode) {
        super(repositoryErrorCode.getMessage());
    }
}
