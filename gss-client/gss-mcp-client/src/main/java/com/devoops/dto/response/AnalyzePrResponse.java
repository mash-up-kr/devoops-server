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

    public AnalyzePrResponse(Usage usage, PrAnalysis prAnalysis) {
        this(
                usage.getPromptTokens(),
                usage.getCompletionTokens(),
                usage.getTotalTokens(),
                prAnalysis
        );
    }
}
