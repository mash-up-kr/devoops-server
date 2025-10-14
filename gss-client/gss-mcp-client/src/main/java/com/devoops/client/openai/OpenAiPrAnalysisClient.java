package com.devoops.client.openai;

import com.devoops.McpClientType;
import com.devoops.client.PrAnalysisClient;
import com.devoops.client.PromptBuilder;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.dto.response.PrAnalysis;
import com.devoops.serdes.PrAnalysisMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.ai.openai.api.ResponseFormat.Type;
import org.springframework.context.annotation.Primary;

@Primary
@Slf4j
public class OpenAiPrAnalysisClient implements PrAnalysisClient {

    private static final McpClientType MCP_CLIENT_VENDOR = McpClientType.OPEN_AI;

    private final ChatClient chatClient;
    private final PromptBuilder promptBuilder;
    private final PrAnalysisMapper prAnalysisMapper;

    public OpenAiPrAnalysisClient(
            OpenAiChatModel openAiChatModel,
            PromptBuilder promptBuilder,
            PrAnalysisMapper prAnalysisMapper
    ) {
        this.chatClient = ChatClient.create(openAiChatModel);
        this.promptBuilder = promptBuilder;
        this.prAnalysisMapper = prAnalysisMapper;
    }

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
        PrAnalysis prAnalysis = prAnalysisMapper.mapToPrAnalysis(analysisResult);
        return new AnalyzePrResponse(usage, prAnalysis);
    }

    @Override
    public McpClientType getMcpClientType() {
        return MCP_CLIENT_VENDOR;
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
