package com.devoops.domain.entity.analysis;

import com.devoops.util.CurrencyUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ClaudeAiModel implements AiModel {

    CLAUDE_SONNET_4("claude-sonnet-4-20250514", 0.000003, 0.000015),
    ;

    private final String name;
    private final double inputTokenCharge; //달러
    private final double outputTokenCharge; //달러

    public double getCharge(int promptToken, int completionTokens) {
        double inputCharge = CurrencyUtil.usdToKrw(inputTokenCharge * promptToken);
        double outputCharge = CurrencyUtil.usdToKrw(outputTokenCharge * completionTokens);
        return inputCharge + outputCharge;
    }
}
