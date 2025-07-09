package com.devoops.domain.repository.auth;

import com.devoops.domain.entity.auth.RefreshToken2;

public interface RefreshTokenDomainRepository {

    RefreshToken2 save(RefreshToken2 refreshToken);

    boolean exists(String tokenValue);

    RefreshToken2 getRefreshToken(String tokenValue);

    void delete(String tokenValue);
}
