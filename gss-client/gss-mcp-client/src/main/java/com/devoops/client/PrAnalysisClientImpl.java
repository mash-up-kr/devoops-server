package com.devoops.client;

import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.dto.response.PrAnalysis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.openai.api.ResponseFormat.Type;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrAnalysisClientImpl implements PrAnalysisClient {

    private final ChatClient chatClient;
    private final PromptBuilder promptBuilder;

    @Override
    public AnalyzePrResponse analyze(AnalyzePrRequest request) {
        //option 설정
        OpenAiChatOptions openAiChatOptions = openAiChatBuilder()
                .model(request.model())
                .build();

        ChatResponse chatresponse = callChatResponse(
                request.title(),
                request.description(),
                request.codeDifference(),
                openAiChatOptions
        );

        Usage usage = chatresponse.getMetadata().getUsage();
        String analysisResult = chatresponse.getResult().getOutput().getText();
        return new AnalyzePrResponse(usage, analysisResult);
    }

    private OpenAiChatOptions.Builder openAiChatBuilder() {
        return OpenAiChatOptions.builder()
                .responseFormat(new ResponseFormat(Type.JSON_SCHEMA, outputJsonSchema()))
                .reasoningEffort("medium")
                .temperature(1.0);

    }

    private String outputJsonSchema() {
        BeanOutputConverter<PrAnalysis> outputConverter = new BeanOutputConverter<>(PrAnalysis.class);
        return outputConverter.getJsonSchema();
    }

    private ChatResponse callChatResponse(String title, String description, String codeDifference,
                                          ChatOptions options) {
        String userPrompt = promptBuilder.buildUserPrompt(title, description, codeDifference);
        String systemPrompt = promptBuilder.buildSystemPrompt();
        return chatClient.prompt()
                .options(options)
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .chatResponse();
    }
}
