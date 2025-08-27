package com.devoops;

import com.devoops.client.claude.ClaudePrAnalysisClient;
import com.devoops.client.openai.OpenAiPrAnalysisClient;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.dto.response.PrAnalysis;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
class OpenAiPrAnalysisClientTest {

    @Autowired
    private OpenAiPrAnalysisClient openAiPrAnalysisClient;

    @Autowired
    private ClaudePrAnalysisClient claudePrAnalysisClient;

    @Test
    void analyzePr_shouldReturnSummaryAndQuestions_openai() {
        // given
        String title = "회원가입 시 이메일 중복 체크 로직 추가";
        String desc = "회원가입 시 이메일 중복 체크 로직 추가입니다.";
        String diff = """
                diff --git a/UserService.java b/UserService.java
                +    public boolean isEmailTaken(String email) {
                +        return userRepository.existsByEmail(email);
                +    }
                +
                +    public void register(User user) {
                +        if (isEmailTaken(user.getEmail())) {
                +            throw new DuplicateEmailException();
                +        }
                +        userRepository.save(user);
                +    }
                """;

        long startTime = System.currentTimeMillis();
        AnalyzePrRequest request = new AnalyzePrRequest(title, desc, diff, "gpt-5-nano");
        PrAnalysis result = openAiPrAnalysisClient.analyze(request).prAnalysis();
        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime+ "ms");

        System.out.println("📝 요약: " + result.summary());
        result.summaryDetails().forEach(q -> System.out.println("- " + q));
        result.questions().forEach(q -> System.out.println("- " + q));
    }

    @Test
    void analyzePr_shouldReturnSummaryAndQuestions_claude() {
        // given
        String title = "회원가입 시 이메일 중복 체크 로직 추가";
        String desc = "회원가입 시 이메일 중복 체크 로직 추가입니다.";
        String diff = """
                diff --git a/UserService.java b/UserService.java
                +    public boolean isEmailTaken(String email) {
                +        return userRepository.existsByEmail(email);
                +    }
                +
                +    public void register(User user) {
                +        if (isEmailTaken(user.getEmail())) {
                +            throw new DuplicateEmailException();
                +        }
                +        userRepository.save(user);
                +    }
                """;

        long startTime = System.currentTimeMillis();
        AnalyzePrRequest request = new AnalyzePrRequest(title, desc, diff, "claude-sonnet-4-20250514");
        PrAnalysis result = claudePrAnalysisClient.analyze(request).prAnalysis();
        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime+ "ms");

        System.out.println("📝 요약: " + result.summary());
        result.summaryDetails().forEach(q -> System.out.println("- " + q));
        result.questions().forEach(q -> System.out.println("- " + q));
    }
}
