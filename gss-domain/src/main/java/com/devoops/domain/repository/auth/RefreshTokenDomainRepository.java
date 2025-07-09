package com.devoops.domain.repository.auth;

import com.devoops.domain.entity.auth.RefreshToken2;

public interface RefreshTokenDomainRepository {

    void save(RefreshToken2 refreshToken);

    RefreshToken2 getRefreshToken(long userId);

    void delete(long userId);
}
