package com.devoops.service.auth.jwt;

public enum TokenType {

    ACCESS_TOKEN,
    REFRESH_TOKEN,
    ;

    public boolean isAccess() {
        return this == TokenType.ACCESS_TOKEN;
    }
}
