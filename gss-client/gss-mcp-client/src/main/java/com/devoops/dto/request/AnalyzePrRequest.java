package com.devoops.dto.request;

public record AnalyzePrRequest(
        String title,
        String description,
        String codeDifference,
        String model
) {

}
