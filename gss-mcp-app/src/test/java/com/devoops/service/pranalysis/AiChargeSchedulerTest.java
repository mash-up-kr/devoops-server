package com.devoops.service.pranalysis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseMcpTest;
import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.repository.analysis.AiChargeRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AiChargeSchedulerTest extends BaseMcpTest {

    @Autowired
    private AiChargeScheduler aiChargeScheduler;

    @Autowired
    private AiChargeRepository aiChargeRepository;

    @Nested
    class CreateNextMonthCharge {

        @Test
        void 다음달_비용을_초기화한다() {
            ZoneId zoneId = ZoneId.of("Asia/Seoul");
            YearMonth yearMonth = YearMonth.from(LocalDate.now(zoneId));
            YearMonth nextMonth = yearMonth.plusMonths(1);

            aiChargeScheduler.createNextMonthCharge();

            AiCharge nextMonthAiCharge = aiChargeRepository.getByYearAndMonth(
                    nextMonth.getYear(),
                    nextMonth.getMonthValue()
            );
            assertAll(
                    () -> assertThat(nextMonthAiCharge.getYear()).isEqualTo(nextMonth.getYear()),
                    () -> assertThat(nextMonthAiCharge.getMonth()).isEqualTo(nextMonth.getMonthValue()),
                    () -> assertThat(nextMonthAiCharge.getCharge().doubleValue()).isEqualTo(0.0)
            );
        }
    }

}
