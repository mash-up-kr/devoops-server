package com.devoops.client;

import com.devoops.McpClientType;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.dto.response.AnalyzePrResponse;

public interface PrAnalysisClient {

    AnalyzePrResponse analyze(AnalyzePrRequest request);

    McpClientType getMcpClientType();
}
