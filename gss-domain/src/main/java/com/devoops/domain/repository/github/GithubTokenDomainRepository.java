package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;

import java.util.Optional;

public interface GithubTokenDomainRepository {

    GithubToken save(GithubToken token, User user);

    GithubToken getByUserId(long userId);
}
