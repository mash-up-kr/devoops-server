package com.devoops.fake;

import com.devoops.client.GithubOAuthClient;
import com.devoops.dto.response.UserInfoResponse;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeOAuthClient implements GithubOAuthClient {

    @Override
    public UserInfoResponse getUserInfo(String accessToken) {
        return new UserInfoResponse(1L, "testName", "avatarUrl");
    }
}
