package com.devoops.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyUtil {

    // 25-08-21 기준
    private static final double CURRENCY_RATE = 1397.84;

    /**
     * 달러를 원으로 변환
     * @param usd 달러 금액
     * @return KRW 금액
     */
    public static double usdToKrw(double usd) {
        return usd * CURRENCY_RATE;
    }
}
