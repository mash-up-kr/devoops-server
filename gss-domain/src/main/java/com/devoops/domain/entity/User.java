package com.devoops.domain.entity;

/**
 * pojo domain entity
 */

public class User {

    private String externalId;

    public String getExternalId() {
        return externalId;
    }

    public User(String externalId) {
        this.externalId = externalId;
    }
}
