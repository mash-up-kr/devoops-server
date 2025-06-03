package com.devoops.dto.request;

public record GithubTokenRequest(
        String code,
        String redirect_uri
) {

}
