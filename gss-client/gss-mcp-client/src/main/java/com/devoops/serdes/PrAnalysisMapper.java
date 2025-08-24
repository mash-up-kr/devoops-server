package com.devoops.serdes;

import com.devoops.dto.response.PrAnalysis;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PrAnalysisMapper {

    private static final int MAX_LOGGING_LENGTH = 255;

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public PrAnalysis mapToPrAnalysis(String analysisResult) {
        try {
            return MAPPER.readValue(analysisResult, PrAnalysis.class);
        } catch (JsonProcessingException e) {
            log.error("PR 질문 생성 파싱 오류 : {}", analysisResult.substring(0, MAX_LOGGING_LENGTH));
            throw new GssException(ErrorCode.AI_RESPONSE_PARSING_ERROR);
        }
    }
}
