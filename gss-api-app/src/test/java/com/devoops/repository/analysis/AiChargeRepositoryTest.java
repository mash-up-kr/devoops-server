package com.devoops.repository.analysis;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.repository.analysis.AiChargeRepository;
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

            assertThat(actual.getCharge()).isEqualTo(charge);
        }

        @Test
        void 가져올_요금이_없다면_초기화한다() {
            LocalDate localDate = LocalDate.now();

            AiCharge actual = chargeRepository.getByYearAndMonth(localDate.getYear(), localDate.getMonthValue());

            assertThat(actual.getCharge()).isEqualTo(0.0);
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
            assertThat(updatedcharge.getCharge()).isEqualTo(charge * 2);
        }
    }
}
