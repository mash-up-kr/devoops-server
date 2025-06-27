package com.devoops.client;

import com.devoops.dto.response.AnalyzePrResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
class AnalysisPrTool {

    @Tool(name = "analyze_pr", description = "Analyze PR and extract summary and improvement points.")
    public AnalyzePrResponse analyzePr(
        @ToolParam(description = "PR title") String title,
        @ToolParam(description = "PR code diff") String diff
    ) {
        log.info("Tool function 'analyze_pr' invoked.");
        return new AnalyzePrResponse("summary", List.of("consideration1"));
    }
}
