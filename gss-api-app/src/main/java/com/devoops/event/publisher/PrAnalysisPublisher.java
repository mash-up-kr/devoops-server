package com.devoops.event.publisher;


import com.devoops.dto.AppWebhookEventRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrAnalysisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    public void publish(List<AppWebhookEventRequest> eventList) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), eventList);
    }
}

