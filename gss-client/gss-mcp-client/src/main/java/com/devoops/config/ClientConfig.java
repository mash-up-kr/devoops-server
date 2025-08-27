package com.devoops.config;

import com.devoops.client.PrAnalysisClient;
import com.devoops.client.PromptBuilder;
import com.devoops.client.claude.ClaudePrAnalysisClient;
import com.devoops.client.openai.OpenAiPrAnalysisClient;
import com.devoops.serdes.PrAnalysisMapper;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
public class ClientConfig {

    @Bean
    @Order(1)
    public PrAnalysisClient openAiClient(
            OpenAiChatModel openAiChatModel,
            PromptBuilder promptBuilder,
            PrAnalysisMapper prAnalysisMapper
    ) {
        return new OpenAiPrAnalysisClient(openAiChatModel, promptBuilder, prAnalysisMapper);
    }

    @Bean
    @Order(2)
    public PrAnalysisClient claudeClient(
            AnthropicChatModel anthropicChatModel,
            PromptBuilder promptBuilder,
            PrAnalysisMapper prAnalysisMapper
    ) {
        return new ClaudePrAnalysisClient(anthropicChatModel, promptBuilder, prAnalysisMapper);
    }
}
