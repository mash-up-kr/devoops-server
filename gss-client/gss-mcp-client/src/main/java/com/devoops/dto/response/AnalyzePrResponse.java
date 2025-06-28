package com.devoops.dto.response;


import java.util.List;

public record AnalyzePrResponse(
    String summary,
    List<SummaryDetails> summaryDetails,
    List<TaggedQuestion> questions
) {
    public record SummaryDetails(
        String title,
        String description
    ) {
    }

    public record TaggedQuestion(
        String tag,
        List<String> question
    ) {
    }
}

