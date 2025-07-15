package com.devoops.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration
class RedisSubConfig {

    private static final String CHANNEL_TOPIC = "pr-analysis";

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory factory, RedisListener listener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(listener, new ChannelTopic(CHANNEL_TOPIC));
        return container;
    }
}
