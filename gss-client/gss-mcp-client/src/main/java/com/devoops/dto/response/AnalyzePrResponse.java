package com.devoops.dto.response;


import java.util.List;

public record AnalyzePrResponse(
    String summary,
    List<SummaryDetails> summaryDetails,
    List<CategorizedQuestion> questions
) {
    public record SummaryDetails(
        String title,
        String description
    ) {
    }

    public record CategorizedQuestion(
        String category,
        List<String> question
    ) {
    }
}

