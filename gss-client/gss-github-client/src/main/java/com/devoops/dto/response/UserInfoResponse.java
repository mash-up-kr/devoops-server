package com.devoops.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

//github spec : https://docs.github.com/ko/rest/users/users?apiVersion=2022-11-28
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserInfoResponse(
        long id,
        String name,
        String avatarUrl
) {

}
