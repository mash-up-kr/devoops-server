package com.devoops.domain.entity.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GithubToken {

    private final String token;
}
