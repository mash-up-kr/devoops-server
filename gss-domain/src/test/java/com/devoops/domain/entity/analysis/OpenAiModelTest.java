package com.devoops.domain.entity.analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OpenAiModelTest {

    @Nested
    class ModelSelection {

        @Test
        void GPT5_범위에서_선택된다() {
            BigDecimal lowerBound = BigDecimal.valueOf(OpenAiModel.GPT_5.getMoneyUnderCriteria());
            BigDecimal upperBound = BigDecimal.valueOf(OpenAiModel.GPT_5.getMoneyUpperCriteria());

            assertAll(
                    () -> assertThat(OpenAiModel.getModelByUsage(lowerBound)).isEqualTo(OpenAiModel.GPT_5),
                    () -> assertThat(OpenAiModel.getModelByUsage(upperBound)).isEqualTo(OpenAiModel.GPT_5)
            );
        }

        @Test
        void GPT5_MINI_범위에서_선택된다() {
            BigDecimal lowerBound = BigDecimal.valueOf(OpenAiModel.GPT_5_MINI.getMoneyUnderCriteria());
            BigDecimal upperBound = BigDecimal.valueOf(OpenAiModel.GPT_5_MINI.getMoneyUpperCriteria());

            assertAll(
                    () -> assertThat(OpenAiModel.getModelByUsage(lowerBound)).isEqualTo(OpenAiModel.GPT_5_MINI),
                    () -> assertThat(OpenAiModel.getModelByUsage(upperBound)).isEqualTo(OpenAiModel.GPT_5_MINI)
            );
        }

        @Test
        void GPT5_NANO_범위에서_선택된다() {
            BigDecimal lowerBound = BigDecimal.valueOf(OpenAiModel.GPT_5_NANO.getMoneyUnderCriteria());
            BigDecimal upperBound = BigDecimal.valueOf(OpenAiModel.GPT_5_NANO.getMoneyUpperCriteria());

            assertAll(
                    () -> assertThat(OpenAiModel.getModelByUsage(lowerBound)).isEqualTo(OpenAiModel.GPT_5_NANO),
                    () -> assertThat(OpenAiModel.getModelByUsage(upperBound)).isEqualTo(OpenAiModel.GPT_5_NANO)
            );
        }
    }
}
