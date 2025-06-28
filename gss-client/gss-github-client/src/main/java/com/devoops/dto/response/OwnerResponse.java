package com.devoops.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OwnerResponse(
        @JsonProperty("login")
        String name
) {

}
