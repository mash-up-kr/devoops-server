package com.devoops.dto.response;

//github spec : https://docs.github.com/ko/rest/users/users?apiVersion=2022-11-28
public record MemberInfoResponse(
        String email,
        String reposUrl
) {

}
