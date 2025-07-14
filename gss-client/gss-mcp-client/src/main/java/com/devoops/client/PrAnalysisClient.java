package com.devoops.client;

import com.devoops.dto.response.AnalyzePrResponse;

public interface PrAnalysisClient {

    AnalyzePrResponse analyze(String title, String description, String diff);
}
