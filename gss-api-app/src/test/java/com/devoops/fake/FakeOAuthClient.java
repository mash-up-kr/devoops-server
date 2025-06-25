package com.devoops.fake;

import com.devoops.client.GithubOAuthClient;
import com.devoops.dto.request.GithubTokenRequest;
import com.devoops.dto.response.GithubTokenResponse;
import com.devoops.dto.response.UserInfoResponse;

public class FakeOAuthClient implements GithubOAuthClient {

    @Override
    public GithubTokenResponse getToken(GithubTokenRequest githubTokenRequest) {
        return new GithubTokenResponse("testToken", "bearer");
    }

    @Override
    public UserInfoResponse getUserInfo(String accessToken) {
        return new UserInfoResponse(1L, "testName", "avatarUrl");
    }
}
