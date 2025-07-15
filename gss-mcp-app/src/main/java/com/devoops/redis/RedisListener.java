package com.devoops.redis;

import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.service.webhook.WebhookFacadeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final WebhookFacadeService webhookFacadeService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            List<AppWebhookEventRequest> result = objectMapper.readValue(
                    message.getBody(),
                    new TypeReference<>() {
                    }
            );
            result.forEach(webhookFacadeService::createQuestionWithWebhookEvent);

        } catch (IOException e) {
            e.printStackTrace();
            throw new GssException(ErrorCode.REDIS_PUBLISH_ERROR);
        }
    }
}

