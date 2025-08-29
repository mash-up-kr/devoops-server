package com.devoops.service.pranalysis;

import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.repository.analysis.AiChargeRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChargeService {

    private final AiChargeRepository chargeRepository;

    public AiCharge getMonthlyCharge() {
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        LocalDate today = LocalDate.now(seoulZoneId);
        return chargeRepository.getByYearAndMonth(today.getYear(), today.getMonthValue());
    }

    public void addCharge(double consumedCharge) {
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        LocalDate today = LocalDate.now(seoulZoneId);
        chargeRepository.addCharge(today.getYear(), today.getMonthValue(), consumedCharge);
    }
}
