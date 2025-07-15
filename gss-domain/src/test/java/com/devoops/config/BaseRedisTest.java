package com.devoops.config;

import com.devoops.redis.auth.BlackListRepositoryImpl;
import com.devoops.redis.auth.RefreshTokenDomainRepositoryImpl;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("redis")
@Import({TestConfig.class, RedisConfig.class})
@SpringBootTest(
        classes = {RefreshTokenDomainRepositoryImpl.class, BlackListRepositoryImpl.class},
        webEnvironment = WebEnvironment.NONE
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseRedisTest {

    static final GenericContainer<?> redisContainer;

    static {
        redisContainer = new GenericContainer<>("redis:7.2")
                .withExposedPorts(6379);
        redisContainer.start();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
    }
}
