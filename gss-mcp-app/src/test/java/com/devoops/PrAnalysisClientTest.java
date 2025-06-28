package com.devoops;

import com.devoops.client.PrAnalysisClient;
import com.devoops.dto.response.AnalyzePrResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Disabled
@SpringBootTest
class PrAnalysisClientTest {

    @Autowired
    PrAnalysisClient prAnalysisClient;

    @Test
    void analyzePr_shouldReturnSummaryAndQuestions() {
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

        AnalyzePrResponse result = prAnalysisClient.analyze(title, desc, diff);

        System.out.println("📝 요약: " + result.summary());
        result.questions().forEach(q -> System.out.println("- " + q));
    }
}
