package com.devoops.client;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    @Value("${dev-oops.github-pr-analysis.prompt}")
    private String promptTemplate;

    @Value("${dev-oops.github-pr-analysis.system}")
    private String systemPrompt;

    public String buildUserPrompt(String title, String description, String diff) {
        return promptTemplate
                .replace("{title}", title)
                .replace("{description}", description)
                .replace("{diff}", encodeDiff(diff));
    }

    public String buildSystemPrompt() {
        return systemPrompt;
    }

    private String encodeDiff(String diff) {
        return Base64.getEncoder().encodeToString(diff.getBytes(StandardCharsets.UTF_8));
    }
}
