package com.devoops.repository.analysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.repository.analysis.AiChargeRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AiChargeRepositoryTest extends BaseServiceTest {

    @Autowired
    private AiChargeRepository chargeRepository;

    @Nested
    class GetByMonth {

        @Test
        void 월에_해당하는_요금을_가져온다() {
            double charge = 1500.0;
            LocalDate localDate = LocalDate.now();
            aiChargeGenerator.generate(localDate.getYear(), localDate.getMonthValue(), charge);

            AiCharge actual = chargeRepository.getByYearAndMonth(localDate.getYear(), localDate.getMonthValue());

            assertThat(actual.getCharge().doubleValue()).isEqualTo(charge);
        }

        @Test
        void 가져올_요금이_없다면_에러가_발생한다() {
            LocalDate localDate = LocalDate.now();

            assertThatThrownBy(() -> chargeRepository.getByYearAndMonth(localDate.getYear(), localDate.getMonthValue()))
                    .isInstanceOf(GssException.class)
                    .hasMessage(ErrorCode.AI_CHARGE_NOT_FOUND.getMessage());
        }

    }

    @Nested
    class Update {

        @Test
        void 요금을_업데이트_할_수_있다() {
            double charge = 1500.0;
            LocalDate localDate = LocalDate.now();
            aiChargeGenerator.generate(localDate.getYear(), localDate.getMonthValue(), charge);

            chargeRepository.addCharge(localDate.getYear(), localDate.getMonthValue(), charge);

            AiCharge updatedcharge = chargeRepository.getByYearAndMonth(localDate.getYear(), localDate.getMonthValue());
            assertThat(updatedcharge.getCharge().doubleValue()).isEqualTo(charge * 2);
        }
    }
}
