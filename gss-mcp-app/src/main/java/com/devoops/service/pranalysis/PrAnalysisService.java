package com.devoops.service.pranalysis;

import com.devoops.adaptor.GithubAdaptor;
import com.devoops.adaptor.PrAnalysisAdapter;
import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.entity.analysis.OpenAiModel;
import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.domain.repository.analysis.AiChargeRepository;
import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrAnalysisService {

    private final GithubAdaptor githubAdaptor;
    private final PrAnalysisAdapter prAnalysisAdapter;
    private final AiChargeRepository chargeRepository;

    public AdaptedAnalyzePrResponse analyzePullRequest(AppWebhookEventRequest request, GithubToken githubToken) {
        String diff = githubAdaptor.getCodeChangeHistory(request.diffUrl(), githubToken.getToken());
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        LocalDate today = LocalDate.now(seoulZoneId);
        AiCharge aiCharge = chargeRepository.getByYearAndMonth(today.getYear(), today.getMonthValue());
        OpenAiModel aiModel = OpenAiModel.getModelByUsage(aiCharge.getCharge());

        AdaptedAnalyzePrResponse result = prAnalysisAdapter.analyze(
                request.title(),
                request.description(),
                diff,
                aiModel.getName()
        );

        double consumedCharge = aiModel.getCharge(result.promptTokens(), result.completionTokens());
        chargeRepository.addCharge(today.getYear(), today.getMonthValue(), consumedCharge);
        return result;
    }
}
