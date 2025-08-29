package com.devoops.service.pranalysis;

import com.devoops.adaptor.GithubAdaptor;
import com.devoops.adaptor.PrAnalysisAdapter;
import com.devoops.domain.entity.analysis.AiCharge;
import com.devoops.domain.entity.analysis.OpenAiModel;
import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.domain.repository.analysis.AiChargeRepository;
import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrAnalysisFacadeService {

    private final GithubAdaptor githubAdaptor;
    private final PrAnalysisAdapter prAnalysisAdapter;
    private final AiChargeService aiChargeService;

    public AdaptedAnalyzePrResponse analyzePullRequest(AppWebhookEventRequest request, GithubToken githubToken) {
        String diff = githubAdaptor.getCodeChangeHistory(request.diffUrl(), githubToken.getToken());
        AiCharge aiCharge = aiChargeService.getMonthlyCharge();
        OpenAiModel aiModel = OpenAiModel.getModelByUsage(aiCharge.getCharge());

        AdaptedAnalyzePrResponse result = prAnalysisAdapter.analyze(
                request.title(),
                request.description(),
                diff,
                aiCharge.getCharge()
        );

        double consumedCharge = aiModel.getCharge(result.promptTokens(), result.completionTokens());
        aiChargeService.addCharge(consumedCharge);
        return result;
    }
}
