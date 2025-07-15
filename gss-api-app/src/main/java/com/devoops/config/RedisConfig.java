package com.devoops.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;

@Configuration
public class RedisConfig {

    private static final String CHANNEL_TOPIC = "pr-analysis";

    @Bean
    public ChannelTopic webhookTopic() {
        return new ChannelTopic(CHANNEL_TOPIC);
    }
}
