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
        int month = LocalDate.now().getMonthValue();
        AiCharge aiCharge = chargeRepository.getByMonth(month);
        OpenAiModel aiModel = OpenAiModel.getModelByUsage(aiCharge.getCharge());

        AdaptedAnalyzePrResponse result = prAnalysisAdapter.analyze(
                request.title(),
                request.description(),
                diff,
                aiModel.getName()
        );
        double consumedCharge = aiModel.getCharge(result.promptTokens(), result.completionTokens());
        chargeRepository.addCharge(month, consumedCharge);
        return result;
    }
}
