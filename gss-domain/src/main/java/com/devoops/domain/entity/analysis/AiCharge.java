package com.devoops.domain.entity.analysis;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AiCharge {

    private final int month;
    private final double charge;
}
