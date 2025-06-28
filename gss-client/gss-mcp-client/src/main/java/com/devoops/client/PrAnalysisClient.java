package com.devoops.client;

import com.devoops.dto.response.AnalyzePrResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrAnalysisClient {

    @Value("${dev-oops.github-pr-analysis.prompt}")
    private String promptTemplate;

    private final ChatModel chatModel;
    private final ObjectMapper objectMapper;

    public AnalyzePrResponse analyze(String title, String description, String diff) {
        String prompt = buildPrompt(title, description, diff);
        String content = chatModel.call(prompt);
        return parseResponse(content);
    }

    private String buildPrompt(String title, String description, String diff) {
        return String.format(promptTemplate, title, description, diff);
    }

    private AnalyzePrResponse parseResponse(String content) {
        try {
            return objectMapper.readValue(content, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("AI 응답 파싱 실패. 응답 내용: {}", content, e);
            throw new IllegalArgumentException("AI 응답 파싱 중 오류 발생", e);
        }
    }
}
