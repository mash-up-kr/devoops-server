package com.devoops.domain.repository.analysis;

import com.devoops.domain.entity.analysis.AiCharge;

public interface AiChargeRepository {

    AiCharge getByYearAndMonth(int year, int month);

    void addCharge(int year, int month, double charge);
}
