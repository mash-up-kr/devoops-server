package com.devoops.domain.repository.auth;

import com.devoops.domain.entity.auth.RefreshToken;
import java.util.Optional;

public interface RefreshTokenDomainRepository {

    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> getRefreshToken(String tokenValue);

    void delete(String tokenValue);
}
