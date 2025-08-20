package com.devoops.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.metadata.Usage;

public record AnalyzePrResponse(
        int promptTokens,
        int completionTokens,
        int totalTokens,
        PrAnalysis prAnalysis
) {

    public AnalyzePrResponse(Usage usage, String analysisResult) {
        this(
                usage.getPromptTokens(),
                usage.getCompletionTokens(),
                usage.getTotalTokens(),
                resolvePrAnalysis(analysisResult)
        );
    }

    private static PrAnalysis resolvePrAnalysis(String analysisResult) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(analysisResult, PrAnalysis.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
