package com.devoops.client.auth;

import com.devoops.domain.entity.auth.GithubToken;
import com.devoops.domain.entity.auth.UserInfo;

public interface AuthClient {

    GithubToken getToken(String code, String redirectUrl);

    UserInfo getUserInfo(GithubToken token);
}
