package com.devoops.client.claude;

import com.devoops.client.PrAnalysisClient;
import com.devoops.client.PromptBuilder;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.dto.response.PrAnalysis;
import com.devoops.serdes.PrAnalysisMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ClaudePrAnalysisClient implements PrAnalysisClient {

    private final ChatClient chatClient;
    private final PromptBuilder promptBuilder;
    private final PrAnalysisMapper prAnalysisMapper;

    public ClaudePrAnalysisClient(
            AnthropicChatModel anthropicChatModel,
            PromptBuilder promptBuilder,
            PrAnalysisMapper prAnalysisMapper
    ) {
        this.chatClient = ChatClient.create(anthropicChatModel);
        this.promptBuilder = promptBuilder;
        this.prAnalysisMapper = prAnalysisMapper;
    }

    @Override
    public AnalyzePrResponse analyze(AnalyzePrRequest request) {
        //option 설정
        ChatOptions anthropicChatOptions = anthropicChatOptions(request.model());

        ChatResponse chatresponse = callChatResponse(
                request.title(),
                request.description(),
                request.codeDifference(),
                anthropicChatOptions
        );
        //claude-sonnet-4-20250514
        Usage usage = chatresponse.getMetadata().getUsage();
        String analysisResult = chatresponse.getResult().getOutput().getText();
        PrAnalysis prAnalysis = prAnalysisMapper.mapToPrAnalysis(analysisResult);
        return new AnalyzePrResponse(usage, prAnalysis);
    }

    private ChatOptions anthropicChatOptions(String model) {
        //"claude-sonnet-4-20250514"
        return ChatOptions.builder()
                .temperature(0.7)
                .model(model)
                .maxTokens(64_000)
                .build();
    }

    private ChatResponse callChatResponse(
            String title,
            String description,
            String codeDifference,
            ChatOptions options
    ) {
        String userPrompt = promptBuilder.buildUserPrompt(title, description, codeDifference);
        String systemPrompt = promptBuilder.buildSystemPromptWithResponseFormat(outputJsonSchema());
        log.info("systemPrompt: {}", systemPrompt);
        return chatClient.prompt()
                .options(options)
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .chatResponse();
    }

    private String outputJsonSchema() {
        BeanOutputConverter<PrAnalysis> outputConverter = new BeanOutputConverter<>(PrAnalysis.class);
        return outputConverter.getJsonSchema();
    }
}
