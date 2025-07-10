package com.devoops.domain.repository.auth;

import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;

public interface BlackListRepository {

    boolean isExists(String token);

    void addBlackList(String token, Duration ttl);
}
