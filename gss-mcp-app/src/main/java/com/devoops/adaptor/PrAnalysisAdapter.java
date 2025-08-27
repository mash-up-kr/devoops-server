package com.devoops.adaptor;

import com.devoops.McpClientType;
import com.devoops.client.PrAnalysisClient;
import com.devoops.domain.entity.analysis.AiModel;
import com.devoops.domain.entity.analysis.OpenAiModel;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import com.devoops.dto.request.AnalyzePrRequest;
import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrAnalysisAdapter {

    private final List<PrAnalysisClient> prAnalysisClients;
    private final AiModelSelector aiModelSelector;

    public AdaptedAnalyzePrResponse analyze(String title, String description, String diff, BigDecimal aiCharge) {
        for (PrAnalysisClient prAnalysisClient : prAnalysisClients) {
            try {
                McpClientType mcpClientType = prAnalysisClient.getMcpClientType();
                AiModel aiModel = aiModelSelector.getAiModel(mcpClientType, aiCharge);
                AnalyzePrRequest analyzePrRequest = new AnalyzePrRequest(title, description, diff, aiModel.getName());
                AnalyzePrResponse analyzePrResponse = prAnalysisClient.analyze(analyzePrRequest);
                return new AdaptedAnalyzePrResponse(analyzePrResponse);
            } catch (Exception e) {
                log.error("client type {} 질문 생성 중 오류 발생 : {}", prAnalysisClient.getMcpClientType(), e.getMessage());;
            }
        }
        throw new GssException(ErrorCode.AI_CREATE_QUESTION_ERROR);
    }
}
