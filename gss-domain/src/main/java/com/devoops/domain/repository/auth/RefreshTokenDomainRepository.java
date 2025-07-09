package com.devoops.domain.repository.auth;

import com.devoops.domain.entity.auth.RefreshToken2;

public interface RefreshTokenDomainRepository {

    void save(RefreshToken2 refreshToken);

    boolean exists(String refreshToken);

    RefreshToken2 getRefreshToken(String tokenValue);

    void delete(long userId);
}
