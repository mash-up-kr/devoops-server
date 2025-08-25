package com.devoops.etc;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OpenaiPropertiesLogger {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @PostConstruct
    public void logProperties() {
        log.info("🔑 OpenAI API Key: {}", maskKey(apiKey));
    }

    private String maskKey(String key) {
        if (key == null || key.length() < 8) return "****";
        return key.substring(0, 4) + "****" + key.substring(key.length() - 4);
    }
}
