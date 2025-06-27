package com.devoops.client;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class McpClientConfig {

    @Bean
    public ChatClient prAnalyzerClient(ChatModel chatModel, AnalysisPrTool tool) {
        return ChatClient.builder(chatModel)
            .defaultTools(tool)
            .build();
    }
}

