package com.devoops.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CurrencyUtil {

    // 25-08-21 기준
    private static final BigDecimal CURRENCY_RATE = BigDecimal.valueOf(1397.84);
    private static final int CONCURRENCY_ROUNDING_SCALE = 2;

    /**
     * 달러를 원으로 변환
     *
     * @param usd 달러 금액
     * @return KRW 금액
     */
    public static double usdToKrw(double usd) {
        BigDecimal usdDecimal = BigDecimal.valueOf(usd);
        return usdDecimal.multiply(CURRENCY_RATE)
                .setScale(CONCURRENCY_ROUNDING_SCALE, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
