package com.devoops.config;

import com.devoops.domain.entity.auth.RefreshToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    private static final String CHANNEL_TOPIC = "pr-analysis";

    @Bean
    public ChannelTopic webhookTopic() {
        return new ChannelTopic(CHANNEL_TOPIC);
    }

    @Bean
    @Qualifier("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper()));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @Qualifier("redisRefreshTokenTemplate")
    public RedisTemplate<String, RefreshToken> redisRefreshTokenTemplate(
            RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, RefreshToken> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(refreshTokenRedisObjectMapper()));
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @Profile("dev")
    public RedisConnectionFactory redisConnectionProdFactory(RedisProperties properties) {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .useSsl()  //SSL을 강제
                .disablePeerVerification() //AWS에서는 신뢰기관인지 검증이 필요 X
                .build();

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(properties.host(),
                properties.port());
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    @Bean
    @Profile("local")
    public RedisConnectionFactory redisConnectionLocalFactory(RedisProperties properties) {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .build();
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(properties.host(),
                properties.port());
        return new LettuceConnectionFactory(redisConfig, clientConfig);
    }

    private ObjectMapper refreshTokenRedisObjectMapper() {
        BasicPolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(RefreshToken.class)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL);
        return objectMapper;
    }

    private ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }
}
