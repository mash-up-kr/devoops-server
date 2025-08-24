package com.devoops.jpa.repository.analysis;

import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.repository.analysis.AiChargeRepository;
import com.devoops.jpa.entity.analysis.AiChargeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class AiChargeRepositoryImpl implements AiChargeRepository {

    private final AiChargeJpaRepository chargeJpaRepository;

    @Override
    public AiCharge getByYearAndMonth(int year, int month) {
        return chargeJpaRepository.findByYearAndMonth(year, month)
                .orElseGet(() -> {
                    AiCharge initializeCharge = new AiCharge(year, month, 0);
                    return chargeJpaRepository.save(AiChargeEntity.from(initializeCharge));
                }).toDomainEntity();
    }

    @Override
    @Transactional
    public void addCharge(int year, int month, double charge) {
        chargeJpaRepository.updateChargeById(year, month, charge);
    }
}
