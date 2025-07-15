package com.devoops;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.testcontainers.containers.GenericContainer;

@TestConfiguration
public class RedisTestConfiguration {

    private static final GenericContainer<?> redisContainer;

    static {
        redisContainer = new GenericContainer<>("redis:7.2.4")
                .withExposedPorts(6379);
        redisContainer.start();
    }

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        String host = redisContainer.getHost();
        Integer port = redisContainer.getMappedPort(6379);
        return new LettuceConnectionFactory(host, port);
    }
}
