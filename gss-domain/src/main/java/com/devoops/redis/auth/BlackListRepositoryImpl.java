package com.devoops.redis.auth;

import com.devoops.domain.repository.auth.BlackListRepository;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
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

    public void addBlackList(String token, Date expiration) {
        String key = BLACK_LIST_TOKEN_PREFIX + token;
        stringRedisTemplate.opsForValue().set(key, token, getTtl(expiration));
    }

    private Duration getTtl(Date expiration) {
        Instant now = Instant.now();
        Instant expirationDate = expiration.toInstant();
        if(expirationDate.isAfter(now)) {
            return Duration.between(now, expirationDate);
        }
        return Duration.ZERO;
    }
}
