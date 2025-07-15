package com.devoops.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@TestConfiguration
@EnableConfigurationProperties(RedisProperties.class)
public class TestConfig {

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    @Profile("redis")
    public RedisConnectionFactory redisConnectionLocalFactory(RedisProperties properties) {
        System.out.println(properties);
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .build();

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(properties.host(), properties.port());
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }
}
