package com.devoops.config;

import com.devoops.dto.response.PrAnalysis;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.openai.api.ResponseFormat.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public OpenAiChatOptions.Builder openAiChatBuilder() {
        return OpenAiChatOptions.builder()
                .responseFormat(new ResponseFormat(Type.JSON_SCHEMA, outputJsonSchema()))
                .reasoningEffort("medium")
                .temperature(1.0);

    }

    public String outputJsonSchema() {
        BeanOutputConverter<PrAnalysis> outputConverter = new BeanOutputConverter<>(PrAnalysis.class);
        return outputConverter.getJsonSchema();
    }

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}
