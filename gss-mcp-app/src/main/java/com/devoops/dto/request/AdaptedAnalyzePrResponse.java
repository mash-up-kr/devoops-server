package com.devoops.dto.request;

import com.devoops.dto.response.AnalyzePrResponse.CategorizedQuestion;
import java.util.List;

public record AdaptedAnalyzePrResponse(
        String summary,
        String detailSummary,
        List<CategorizedQuestion> questions
) {

}
