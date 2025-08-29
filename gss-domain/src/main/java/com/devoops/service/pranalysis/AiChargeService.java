package com.devoops.service.pranalysis;

import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.repository.analysis.AiChargeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChargeService {

    private static final ZoneId SEOUL_ZONE_ID = ZoneId.of("Asia/Seoul");

    private final AiChargeRepository chargeRepository;

    public AiCharge initializeNextMonth() {
        LocalDate now = LocalDate.now(SEOUL_ZONE_ID);
        YearMonth nextMonth = YearMonth.from(now).plusMonths(1);
        AiCharge aiCharge = new AiCharge(nextMonth.getYear(), nextMonth.getMonthValue(), BigDecimal.ZERO);
        return chargeRepository.save(aiCharge);
    }

    public AiCharge getMonthlyCharge() {
        LocalDate today = LocalDate.now(SEOUL_ZONE_ID);
        return chargeRepository.getByYearAndMonth(today.getYear(), today.getMonthValue());
    }

    public void addCharge(double consumedCharge) {
        LocalDate today = LocalDate.now(SEOUL_ZONE_ID);
        chargeRepository.addCharge(today.getYear(), today.getMonthValue(), consumedCharge);
    }
}
