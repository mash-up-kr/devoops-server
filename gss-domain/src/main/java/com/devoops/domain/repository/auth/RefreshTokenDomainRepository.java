package com.devoops.domain.repository.auth;

import com.devoops.domain.entity.auth.RefreshToken;

public interface RefreshTokenDomainRepository {

    RefreshToken save(RefreshToken refreshToken);

    boolean exists(String tokenValue);

    RefreshToken getRefreshToken(String tokenValue);

    void delete(String tokenValue);
}
