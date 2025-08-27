package com.devoops.adaptor;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.devoops.BaseMcpTest;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

class PrAnalysisAdapterTest extends BaseMcpTest {

    @Autowired
    private PrAnalysisAdapter prAnalysisAdapter;

    @Nested
    class SelectClient {

        @Test
        void openAi에게_최초_호출한다() {
            prAnalysisAdapter.analyze("title", "description", "diff", BigDecimal.ZERO);

            assertAll(
                    () -> Mockito.verify(openAiPrAnalysisClient, times(1)).analyze(any(AnalyzePrRequest.class)),
                    () -> Mockito.verify(claudePrAnalysisClient, never()).analyze(any(AnalyzePrRequest.class))
            );
        }

        @Test
        void openAi가_실패하면_claude에게_질문_생성을_호출한다() {
            Mockito.when(openAiPrAnalysisClient.analyze(any(AnalyzePrRequest.class)))
                    .thenThrow(GssException.class);

            prAnalysisAdapter.analyze("title", "description", "diff", BigDecimal.ZERO);

            assertAll(
                    () -> Mockito.verify(openAiPrAnalysisClient, times(1)).analyze(any(AnalyzePrRequest.class)),
                    () -> Mockito.verify(claudePrAnalysisClient, times(1)).analyze(any(AnalyzePrRequest.class))
            );
        }

        @Test
        void 모든_클라이언트_호출이_실패하면_에러가_발생한다() {
            Mockito.when(openAiPrAnalysisClient.analyze(any(AnalyzePrRequest.class)))
                    .thenThrow(GssException.class);

            Mockito.when(claudePrAnalysisClient.analyze(any(AnalyzePrRequest.class)))
                    .thenThrow(GssException.class);

            assertAll(
                    () -> assertThatThrownBy(
                            () -> prAnalysisAdapter.analyze("title", "description", "diff", BigDecimal.ZERO)
                            )
                            .isInstanceOf(GssException.class)
                            .hasMessage(ErrorCode.AI_CREATE_QUESTION_ERROR.getMessage()),
                    () -> Mockito.verify(openAiPrAnalysisClient, times(1)).analyze(any(AnalyzePrRequest.class)),
                    () -> Mockito.verify(claudePrAnalysisClient, times(1)).analyze(any(AnalyzePrRequest.class))
            );
        }
    }
}
