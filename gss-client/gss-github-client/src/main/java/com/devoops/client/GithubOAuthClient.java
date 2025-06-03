package com.devoops.client;

import com.devoops.dto.request.GithubTokenRequest;
import com.devoops.dto.response.GithubTokenResponse;
import com.devoops.dto.response.UserInfoResponse;

public interface GithubOAuthClient {

    GithubTokenResponse getToken(GithubTokenRequest githubTokenRequest);

    UserInfoResponse getUserInfo(String accessToken);
}
