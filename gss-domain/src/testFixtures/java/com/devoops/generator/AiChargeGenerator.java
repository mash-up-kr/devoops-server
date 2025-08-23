package com.devoops.generator;

import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.jpa.entity.analysis.AiChargeEntity;
import com.devoops.jpa.repository.analysis.AiChargeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AiChargeGenerator {

    @Autowired
    private AiChargeJpaRepository repository;

    public AiCharge generate(int year, int month, double charge) {
        AiCharge aiCharge = new AiCharge(year, month, charge);
        return repository.save(AiChargeEntity.from(aiCharge))
                .toDomainEntity();
    }
}
