package com.devoops.fake;

import com.devoops.client.PrAnalysisClient;
import com.devoops.dto.response.AnalyzePrResponse;
import java.util.Arrays;
import java.util.List;

public class FakePrAnalysisClient implements PrAnalysisClient {

    public static AnalyzePrResponse MOCK_RESPONSE = new AnalyzePrResponse(
            "이 PR은 사용자 인증 로직에 JWT 기반의 토큰 갱신 기능을 추가했습니다.",
            List.of(
                    new AnalyzePrResponse.SummaryDetails(
                            "JWT 리프레시 토큰 기능 추가",
                            "기존 로그인 로직에 리프레시 토큰 발급 및 재발급 기능을 추가하여, 사용자 인증 세션을 안전하게 유지할 수 있도록 개선했습니다."
                    ),
                    new AnalyzePrResponse.SummaryDetails(
                            "보안 강화 및 예외 처리 보완",
                            "토큰 유효성 검사 과정에서 발생할 수 있는 다양한 예외를 핸들링하며, 불필요한 정보 노출을 막기 위한 응답 구조도 정비했습니다."
                    )
            ),
            List.of(
                    new AnalyzePrResponse.CategorizedQuestion(
                            "보안",
                            Arrays.asList(
                                    "JWT 토큰을 사용할 때 고려해야 할 보안 취약점은 무엇인가요?",
                                    "리프레시 토큰 저장 위치와 전달 방식에 따른 장단점은 무엇인가요?"
                            )
                    ),
                    new AnalyzePrResponse.CategorizedQuestion(
                            "유지보수성",
                            Arrays.asList(
                                    "토큰 로직을 추상화하거나 모듈화할 때 고려해야 할 요소는 무엇인가요?",
                                    "예외 처리를 공통화할 때 코드의 가독성과 유지보수성을 어떻게 확보할 수 있나요?"
                            )
                    )
            )
    );

    @Override
    public AnalyzePrResponse analyze(String title, String description, String diff) {
        return MOCK_RESPONSE;
    }
}
