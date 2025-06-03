package com.devoops.client.auth;

import com.devoops.domain.entity.auth.GithubToken;
import com.devoops.domain.entity.auth.UserInfo;
import org.springframework.stereotype.Component;

@Component
public class AuthClientImpl implements AuthClient {
    @Override
    public GithubToken getToken(String code, String redirectUrl) {
        return null;
    }

    @Override
    public UserInfo getUserInfo(GithubToken token) {
        return null;
    }
}
