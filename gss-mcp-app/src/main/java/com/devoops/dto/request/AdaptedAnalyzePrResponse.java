package com.devoops.dto.request;

import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.dto.response.PrAnalysis;
import com.devoops.dto.response.PrAnalysis.CategorizedQuestion;
import com.devoops.util.SummaryFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AdaptedAnalyzePrResponse(
        int promptTokens,
        int completionTokens,
        int totalTokens,
        String summary,
        String detailSummary,
        List<CategorizedQuestion> questions
) {

    public AdaptedAnalyzePrResponse(AnalyzePrResponse analyzePrResponse) {
        this(
                analyzePrResponse.promptTokens(),
                analyzePrResponse.completionTokens(),
                analyzePrResponse.totalTokens(),
                analyzePrResponse.prAnalysis().summary(),
                resolveDetailSummary(analyzePrResponse.prAnalysis().summaryDetails()),
                analyzePrResponse.prAnalysis().questions()
        );
    }

    private static String resolveDetailSummary(List<PrAnalysis.SummaryDetails> summaryDetails) {
        return summaryDetails.stream()
                .map(sd -> Map.entry(sd.title(), sd.description()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), SummaryFormatter::formatWithNumbering));
    }
}
