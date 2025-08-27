package com.devoops.domain.entity.analysis;

public interface AiModel {

    double getCharge(int promptToken, int completionTokens);

    String getName();
}
