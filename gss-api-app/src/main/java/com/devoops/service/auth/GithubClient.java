package com.devoops.service.auth;

import com.devoops.domain.entity.auth.GithubToken;
import com.devoops.domain.entity.auth.UserInfo;
import org.springframework.stereotype.Component;

@Component
public interface GithubClient {

    GithubToken getToken(String code, String redirectUrl);

    UserInfo getMemberInfo(GithubToken token);
}
