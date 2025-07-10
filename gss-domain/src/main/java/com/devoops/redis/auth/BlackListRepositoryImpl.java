package com.devoops.redis.auth;

import com.devoops.domain.repository.auth.BlackListRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlackListRepositoryImpl implements BlackListRepository {

    private static final String BLACK_LIST_TOKEN_PREFIX = "blackListToken";

    private final StringRedisTemplate stringRedisTemplate;

    public boolean isExists(String token) {
        String key = BLACK_LIST_TOKEN_PREFIX + token;
        return stringRedisTemplate.hasKey(key);
    }

    public void addBlackList(String token, Duration ttl) {
        String key = BLACK_LIST_TOKEN_PREFIX + token;
        stringRedisTemplate.opsForValue().set(key, token, ttl);
    }
}
