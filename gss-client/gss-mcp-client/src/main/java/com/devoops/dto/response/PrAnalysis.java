package com.devoops.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PrAnalysis(
        @JsonProperty(required = true) String summary,
        @JsonProperty(required = true) List<SummaryDetails> summaryDetails,
        @JsonProperty(required = true) List<CategorizedQuestion> questions
) {
    public record SummaryDetails(
            @JsonProperty(required = true) String title,
            @JsonProperty(required = true) String description
    ) {
    }

    public record CategorizedQuestion(
            @JsonProperty(required = true) String category,
            @JsonProperty(required = true) List<String> question
    ) {
    }
}

