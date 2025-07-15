package com.devoops.redis.auth;

import com.devoops.domain.entity.auth.RefreshToken;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenDomainRepositoryImpl implements RefreshTokenDomainRepository {

    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh:";

    private final RedisTemplate<String, RefreshToken> redisRefreshTokenTemplate;

    public RefreshToken save(RefreshToken refreshToken) {
        String key = REFRESH_TOKEN_KEY_PREFIX + refreshToken.getValue();
        redisRefreshTokenTemplate.opsForValue().set(key, refreshToken, refreshToken.getTtl());
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> getRefreshToken(String tokenValue) {
        String key = REFRESH_TOKEN_KEY_PREFIX + tokenValue;
        return Optional.ofNullable(redisRefreshTokenTemplate.opsForValue().get(key));
    }

    @Override
    public void delete(String tokenValue) {
        String key = REFRESH_TOKEN_KEY_PREFIX + tokenValue;
        redisRefreshTokenTemplate.delete(key);
    }
}
