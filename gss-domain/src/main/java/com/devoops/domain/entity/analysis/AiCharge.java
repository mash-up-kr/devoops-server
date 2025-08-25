package com.devoops.domain.entity.analysis;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AiCharge {

    private final int year;
    private final int month;
    private final BigDecimal charge;
}
