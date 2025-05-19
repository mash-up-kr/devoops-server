package com.devoops.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TestExample {

    @DisplayName("테스트 실행 결과를 jacocoTest Report로 표현하기 위한 성공 예제입니다")
    @Test
    void successTest() {
    }

    @DisplayName("테스트 실행 결과를 jacocoTest Report로 표현하기 위한 실패 예제입니다")
    @Test
    void failTest() {
        throw new RuntimeException("실패 예제");
    }
}
