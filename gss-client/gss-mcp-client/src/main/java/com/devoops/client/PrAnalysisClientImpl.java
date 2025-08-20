package com.devoops.client;

import com.devoops.dto.response.AnalyzePrResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.openai.api.ResponseFormat.Type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrAnalysisClientImpl implements PrAnalysisClient {

    @Value("${dev-oops.github-pr-analysis.prompt}")
    private String promptTemplate;

    @Value("${dev-oops.github-pr-analysis.system}")
    private String systemPrompt;

    private final ChatClient chatClient;

    @Override
    public AnalyzePrResponse analyze(String title, String description, String diff) {
        //json schema 추출
        BeanOutputConverter<AnalyzePrResponse> outputConverter = new BeanOutputConverter<>(AnalyzePrResponse.class);
        String jsonSchema = outputConverter.getJsonSchema();

        //option 설정
        OpenAiChatOptions openAiChatOptions = OpenAiChatOptions.builder()
                .responseFormat(new ResponseFormat(Type.JSON_SCHEMA, jsonSchema))
                .model("gpt-5-nano")
                .reasoningEffort("medium")
                .temperature(1.0)
                .build();

        //prompt 만들고 보내기
        String userPrompt = buildPrompt(title, description, diff);
        ChatResponse chatresponse = chatClient.prompt()
                .options(openAiChatOptions)
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .chatResponse();


        Usage usage = chatresponse.getMetadata().getUsage();
        System.out.println("Prompt tokens: " + usage.getPromptTokens());
        System.out.println("Completion tokens: " + usage.getCompletionTokens());
        System.out.println("Total tokens: " + usage.getTotalTokens());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(chatresponse.getResult().getOutput().getText(), AnalyzePrResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildPrompt(String title, String description, String diff) {
        return promptTemplate
                .replace("{title}", title)
                .replace("{description}", description)
                .replace("{diff}", encodeDiff(diff));
    }

    private String encodeDiff(String diff) {
        return Base64.getEncoder().encodeToString(diff.getBytes(StandardCharsets.UTF_8));
    }
}
