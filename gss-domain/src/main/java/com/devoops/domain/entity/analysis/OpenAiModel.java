package com.devoops.domain.entity.analysis;

import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OpenAiModel {

    GPT_5(0, 7500, "gpt-5", 0.00000125, 0.00001),
    GPT_5_MINI(7501, 10000, "gpt-5-mini",0.00000025,0.000002),
    GPT_5_NANO(10001, 15000, "gpt-5-nano", 0.00000005, 0.0000004),
    ;

    private final int moneyUnderCriteria; //원
    private final int moneyUpperCriteria; //원
    private final String name;
    private final double inputTokenCharge; //달러
    private final double outputTokenCharge; //달러

    public OpenAiModel getModelByUsage(int currentUsageWon) {
        return Stream.of(values())
                .filter(model -> model.moneyUnderCriteria<=currentUsageWon && model.moneyUpperCriteria>=currentUsageWon)
                .findAny()
                .orElse(GPT_5_NANO);
    }
}
