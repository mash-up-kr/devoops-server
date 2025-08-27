package com.devoops.adaptor;

import com.devoops.McpClientType;
import com.devoops.domain.entity.analysis.AiModel;
import com.devoops.domain.entity.analysis.ClaudeAiModel;
import com.devoops.domain.entity.analysis.OpenAiModel;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class AiModelSelector {

    public AiModel getAiModel(McpClientType clientType, BigDecimal aiCharge) {
        if(clientType.isOpenAi()) {
            return OpenAiModel.getModelByUsage(aiCharge);
        }
        return ClaudeAiModel.CLAUDE_SONNET_4;
    }
}
