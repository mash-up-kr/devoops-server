package com.devoops.service.pranalysis;

import com.devoops.adaptor.GithubAdaptor;
import com.devoops.adaptor.PrAnalysisAdapter;
import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrAnalysisService {

    private final GithubAdaptor githubAdaptor;
    private final PrAnalysisAdapter prAnalysisAdapter;

    public AdaptedAnalyzePrResponse analyzePullRequest(AppWebhookEventRequest request, GithubToken githubToken) {
        String diff = githubAdaptor.getCodeChangeHistory(request.diffUrl(), githubToken.getToken());
        return prAnalysisAdapter.analyze(request.title(), request.description(), diff, "gpt-5");
    }
}
