package com.devoops.dto.request;

import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.dto.response.PrAnalysis.CategorizedQuestion;
import java.util.List;

public record AdaptedAnalyzePrResponse(
        int promptTokens,
        int completionTokens,
        int totalTokens,
        String summary,
        String detailSummary,
        List<CategorizedQuestion> questions
) {

}
