package com.devoops.redis;

import com.devoops.domain.entity.auth.RefreshToken2;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenDomainRepositoryImpl implements RefreshTokenDomainRepository {

    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh:";

    private final RedisTemplate<String, RefreshToken2> redisTemplate;

    public RefreshToken2 save(RefreshToken2 refreshToken) {
        String key = REFRESH_TOKEN_KEY_PREFIX + refreshToken.getValue();
        redisTemplate.opsForValue().set(key, refreshToken, refreshToken.getTtl());
        return refreshToken;
    }

    @Override
    public boolean exists(String refreshToken) {
        String key = REFRESH_TOKEN_KEY_PREFIX + refreshToken;
        return redisTemplate.hasKey(key);
    }

    public RefreshToken2 getRefreshToken(String tokenValue) {
        String key = REFRESH_TOKEN_KEY_PREFIX + tokenValue;
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(long userId) {
        String key = REFRESH_TOKEN_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }
}
