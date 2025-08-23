package com.devoops.jpa.repository.analysis;

import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.repository.analysis.AiChargeRepository;
import com.devoops.jpa.entity.analysis.AiChargeEntity;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class AiChargeRepositoryImpl implements AiChargeRepository {

    private final AiChargeJpaRepository chargeJpaRepository;

    @Override
    public AiCharge getByMonth(int month) {
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime now = LocalDateTime.now(seoulZoneId);
        int todayYear = now.getYear();
        int todayMonth = now.getMonthValue();
        return chargeJpaRepository.findByYearAndMonth(todayYear, todayMonth)
                .orElseGet(() -> {
                    AiCharge initializeCharge = new AiCharge(todayYear, todayMonth, 0);
                    return chargeJpaRepository.save(AiChargeEntity.from(initializeCharge));
                }).toDomainEntity();
    }

    @Override
    @Transactional
    public void addCharge(int month, double charge) {
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        LocalDateTime now = LocalDateTime.now(seoulZoneId);
        int todayYear = now.getYear();
        chargeJpaRepository.updateChargeById(todayYear, month, charge);
    }
}
