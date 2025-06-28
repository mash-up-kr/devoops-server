package com.devoops.dto.response;


import java.util.List;

public record AnalyzePrResponse(
    String summary,
    List<TaggedQuestion> questions
) {
    public record TaggedQuestion(
        String tag,
        List<String> question
    ) {}
}

