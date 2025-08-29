package com.devoops.jpa.repository.analysis;

import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.repository.analysis.AiChargeRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.analysis.AiChargeEntity;
import java.math.BigDecimal;
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
                .orElseThrow(() -> new GssException(ErrorCode.AI_CHARGE_NOT_FOUND))
                .toDomainEntity();
    }

    @Override
    @Transactional
    public void addCharge(int year, int month, double charge) {
        chargeJpaRepository.updateChargeById(year, month, charge);
    }

    @Override
    @Transactional
    public AiCharge save(AiCharge charge) {
        return chargeJpaRepository.save(AiChargeEntity.from(charge))
                .toDomainEntity();
    }
}
