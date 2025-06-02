package com.devoops.dto.response;

import com.devoops.domain.entity.user.User;

public record UserSaveResponse(String email) {

    public UserSaveResponse(User user){
        this(user.getEmail());
    }
}
