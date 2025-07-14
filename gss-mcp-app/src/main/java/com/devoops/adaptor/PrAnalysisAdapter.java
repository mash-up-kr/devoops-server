package com.devoops.adaptor;

import com.devoops.client.PrAnalysisClient;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import com.devoops.dto.response.AnalyzePrResponse;
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

    public AdaptedAnalyzePrResponse analyze(String title, String description, String diff) {
        AnalyzePrResponse analyzePrResponse = prAnalysisClient.analyze(title, description, diff);
        String detailSummary = resolveDetailSummary(analyzePrResponse.summaryDetails());
        return new AdaptedAnalyzePrResponse(
                analyzePrResponse.summary(),
                detailSummary,
                analyzePrResponse.questions()
        );
    }

    private String resolveDetailSummary(List<AnalyzePrResponse.SummaryDetails> summaryDetails) {
        return summaryDetails.stream()
                .map(sd -> Map.entry(sd.title(), sd.description()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), SummaryFormatter::formatWithNumbering));
    }
}
