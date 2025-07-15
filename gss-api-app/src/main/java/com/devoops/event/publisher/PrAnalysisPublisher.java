package com.devoops.event.publisher;


import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrAnalysisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic webhookTopic;
    private final ObjectMapper objectMapper;

    public void publish(List<AppWebhookEventRequest> eventList) {
        try {
            String message = objectMapper.writeValueAsString(eventList);
            redisTemplate.convertAndSend(webhookTopic.getTopic(), message);
        } catch (JsonProcessingException e) {
            throw new GssException(ErrorCode.REDIS_PUBLISH_ERROR);
        }
    }
}

