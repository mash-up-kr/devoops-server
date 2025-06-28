package com.devoops.client;

import com.devoops.dto.response.UserInfoResponse;

public interface GithubOAuthClient {

    UserInfoResponse getUserInfo(String accessToken);
}
