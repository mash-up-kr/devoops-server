package com.devoops.domain.repository.analysis;

import com.devoops.domain.entity.analysis.AiCharge;

public interface AiChargeRepository {

    AiCharge getByMonth(int month);

    void addCharge(long id, double charge);
}
