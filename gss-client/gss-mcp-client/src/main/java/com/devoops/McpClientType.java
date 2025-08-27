package com.devoops;

public enum McpClientType {

    OPEN_AI,
    CLAUDE,
    ;

    public boolean isOpenAi() {
        return this == OPEN_AI;
    }
}
