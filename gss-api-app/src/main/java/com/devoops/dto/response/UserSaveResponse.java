package com.devoops.dto.response;

import com.devoops.domain.entity.user.User;

public record UserSaveResponse(long externalId) {

    public UserSaveResponse(User user){
        this(Long.valueOf(user.getExternalId()));
    }
}
