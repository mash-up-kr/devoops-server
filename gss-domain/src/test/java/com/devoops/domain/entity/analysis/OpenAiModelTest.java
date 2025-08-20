package com.devoops.domain.entity.analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OpenAiModelTest {

    @Nested
    class ModelSelection {

        @Test
        void GPT5_범위에서_선택된다() {
            int lowerBound = OpenAiModel.GPT_5.getMoneyUnderCriteria();
            int upperBound = OpenAiModel.GPT_5.getMoneyUpperCriteria();

            assertAll(
                    () -> assertThat(OpenAiModel.getModelByUsage(lowerBound)).isEqualTo(OpenAiModel.GPT_5),
                    () -> assertThat(OpenAiModel.getModelByUsage(upperBound)).isEqualTo(OpenAiModel.GPT_5)
            );
        }

        @Test
        void GPT5_MINI_범위에서_선택된다() {
            int lowerBound = OpenAiModel.GPT_5_MINI.getMoneyUnderCriteria();
            int upperBound = OpenAiModel.GPT_5_MINI.getMoneyUpperCriteria();

            assertAll(
                    () -> assertThat(OpenAiModel.getModelByUsage(lowerBound)).isEqualTo(OpenAiModel.GPT_5_MINI),
                    () -> assertThat(OpenAiModel.getModelByUsage(upperBound)).isEqualTo(OpenAiModel.GPT_5_MINI)
            );
        }

        @Test
        void GPT5_NANO_범위에서_선택된다() {
            int lowerBound = OpenAiModel.GPT_5_NANO.getMoneyUnderCriteria();
            int upperBound = OpenAiModel.GPT_5_NANO.getMoneyUpperCriteria();

            assertAll(
                    () -> assertThat(OpenAiModel.getModelByUsage(lowerBound)).isEqualTo(OpenAiModel.GPT_5_NANO),
                    () -> assertThat(OpenAiModel.getModelByUsage(upperBound)).isEqualTo(OpenAiModel.GPT_5_NANO)
            );
        }
    }
}
