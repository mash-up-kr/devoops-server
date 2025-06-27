package com.devoops.client;

import com.devoops.dto.response.AnalyzePrResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrAnalysisClient {

    @Value("${dev-oops.github-pr-analysis.prompt}")
    private String prompt;

    private final ChatClient prAnalyzerClient;

    public AnalyzePrResponse analyze(String title, String diff) {
        String template = String.format(prompt, title, diff);
        Prompt prompt = new Prompt(new UserMessage(template));

        // TODO: 프롬프트에서 요구한 응답값이 아닌 경우 예외 처리 구현
        return prAnalyzerClient.prompt(prompt)
            .call()
            .entity(AnalyzePrResponse.class);
    }
}
