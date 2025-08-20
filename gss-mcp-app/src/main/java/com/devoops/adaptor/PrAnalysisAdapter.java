package com.devoops.adaptor;

import com.devoops.client.PrAnalysisClient;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.dto.response.PrAnalysis;
import com.devoops.util.SummaryFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrAnalysisAdapter {

    private final PrAnalysisClient prAnalysisClient;

    public AdaptedAnalyzePrResponse analyze(String title, String description, String diff, String model) {
        AnalyzePrRequest analyzePrRequest = new AnalyzePrRequest(title, description, diff, model);
        AnalyzePrResponse analyzePrResponse = prAnalysisClient.analyze(analyzePrRequest);
        PrAnalysis prAnalysis = analyzePrResponse.prAnalysis();
        String detailSummary = resolveDetailSummary(prAnalysis.summaryDetails());
        return new AdaptedAnalyzePrResponse(
                analyzePrResponse.promptTokens(),
                analyzePrResponse.completionTokens(),
                analyzePrResponse.totalTokens(),
                prAnalysis.summary(),
                detailSummary,
                prAnalysis.questions()
        );
    }

    private String resolveDetailSummary(List<PrAnalysis.SummaryDetails> summaryDetails) {
        return summaryDetails.stream()
                .map(sd -> Map.entry(sd.title(), sd.description()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), SummaryFormatter::formatWithNumbering));
    }
}
