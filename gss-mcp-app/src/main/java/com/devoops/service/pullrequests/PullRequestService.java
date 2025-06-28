package com.devoops.service.pullrequests;

import com.devoops.client.PrAnalysisClient;
import com.devoops.dto.response.AnalyzePrResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PullRequestService {

    private final PrAnalysisClient prAnalysisClient;

    public void handlePullRequestEvent(long userId, String title, String description, String diff) {

        AnalyzePrResponse analyzePrResponse = prAnalysisClient.analyze(title, description, diff);
        // TODO: 저장 로직
    }
}
