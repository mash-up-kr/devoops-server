package com.devoops.redis;

import com.devoops.domain.entity.auth.RefreshToken2;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenDomainRepositoryImpl implements RefreshTokenDomainRepository {

    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh:";

    private final RedisTemplate<String, RefreshToken2> redisTemplate;

    public void save(RefreshToken2 refreshToken) {
        String key = REFRESH_TOKEN_KEY_PREFIX + refreshToken.getUserId();
        redisTemplate.opsForValue().set(key, refreshToken, refreshToken.getTtl());
    }

    public RefreshToken2 getRefreshToken(long userId) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(long userId) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }
}
