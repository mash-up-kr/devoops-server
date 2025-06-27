package com.devoops.dto.response;


import java.util.List;

public record AnalyzePrResponse(
    String summary,
    List<String> considerations
) {
}
