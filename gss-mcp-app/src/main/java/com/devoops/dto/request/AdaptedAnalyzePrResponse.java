package com.devoops.dto.request;

import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.dto.response.AnalyzePrResponse.CategorizedQuestion;
import com.devoops.util.SummaryFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record AdaptedAnalyzePrResponse(
        String summary,
        String detailSummary,
        List<CategorizedQuestion> questions
) {

    public static AdaptedAnalyzePrResponse from(AnalyzePrResponse analyzePrResponse) {
        String detailSummary = resolveDetailSummary(analyzePrResponse.summaryDetails());
        return new AdaptedAnalyzePrResponse(
                analyzePrResponse.summary(),
                detailSummary,
                analyzePrResponse.questions()
        );
    }

    private static String resolveDetailSummary(List<AnalyzePrResponse.SummaryDetails> summaryDetails) {
        return summaryDetails.stream()
                .map(sd -> Map.entry(sd.title(), sd.description()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), SummaryFormatter::formatWithNumbering));
    }
}
