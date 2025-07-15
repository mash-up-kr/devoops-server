package com.devoops.config;

import com.devoops.RedisTestConfiguration;
import com.devoops.redis.auth.BlackListRepositoryImpl;
import com.devoops.redis.auth.RefreshTokenDomainRepositoryImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("redis")
@Import({TestConfig.class, RedisConfig.class, RedisTestConfiguration.class})
@SpringBootTest(
        classes = {RefreshTokenDomainRepositoryImpl.class, BlackListRepositoryImpl.class},
        webEnvironment = WebEnvironment.NONE
)
public abstract class BaseRedisTest {

}
