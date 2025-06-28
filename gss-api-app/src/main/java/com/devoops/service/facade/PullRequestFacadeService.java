package com.devoops.service.facade;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.dto.response.PullRequestDetailReadResponse;
import com.devoops.service.pullrequests.PullRequestService;
import com.devoops.service.question.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PullRequestFacadeService {

    private final PullRequestService pullRequestService;
    private final QuestionService questionService;

    public PullRequestDetailReadResponse detailRead(long pullRequestId) {
        PullRequest pullRequest = pullRequestService.getPullRequest(pullRequestId);
        List<QuestionAnswer> prQuestions = questionService.getAllPrQuestions(pullRequest.getId());
        return PullRequestDetailReadResponse.from(pullRequest, prQuestions);
    }

    public void updateToDone(long pullRequestId) {
        pullRequestService.updateToDone(pullRequestId);
    }
}
