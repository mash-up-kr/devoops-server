package com.devoops.redis;

import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.service.webhook.WebhookFacadeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final WebhookFacadeService webhookFacadeService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String json = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            List<AppWebhookEventRequest> events = objectMapper.readValue(
                    json,
                    new TypeReference<List<AppWebhookEventRequest>>() {}
            );
            events.forEach(webhookFacadeService::createQuestionWithWebhookEvent);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}

