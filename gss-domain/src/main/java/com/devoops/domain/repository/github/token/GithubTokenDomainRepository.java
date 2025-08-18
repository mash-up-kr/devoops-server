package com.devoops.domain.repository.github.token;

import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.domain.entity.user.User;

public interface GithubTokenDomainRepository {

    GithubToken save(GithubToken token, User user);

    GithubToken getByUserId(long userId);
}
