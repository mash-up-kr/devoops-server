package com.devoops.adaptor;

import com.devoops.client.PrAnalysisClient;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.dto.response.AnalyzePrResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrAnalysisAdapter {

    private final PrAnalysisClient prAnalysisClient;

    public AdaptedAnalyzePrResponse analyze(String title, String description, String diff, String model) {
        AnalyzePrRequest analyzePrRequest = new AnalyzePrRequest(title, description, diff, model);
        AnalyzePrResponse analyzePrResponse = prAnalysisClient.analyze(analyzePrRequest);
        return new AdaptedAnalyzePrResponse(analyzePrResponse);
    }
}
