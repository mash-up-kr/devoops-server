package com.devoops.service.facade;

import com.devoops.domain.entity.github.AnswerRankings;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.dto.response.PullRequestDetailReadResponse;
import com.devoops.dto.response.PullRequestRankingResponses;
import com.devoops.dto.response.PullRequestReadResponse;
import com.devoops.service.answerranking.AnswerRankingService;
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
    private final AnswerRankingService answerRankingService;

    public PullRequestReadResponse read(long pullRequestId) {
        PullRequest pullRequest = pullRequestService.getPullRequest(pullRequestId);
        List<QuestionAnswer> prQuestions = questionService.getAllPrQuestions(pullRequest.getId());
        List<String> categories = getUniqueCategories(prQuestions);
        return PullRequestReadResponse.from(pullRequest, categories, prQuestions);
    }

    public PullRequestDetailReadResponse detailRead(long pullRequestId) {
        PullRequest pullRequest = pullRequestService.getPullRequest(pullRequestId);
        List<QuestionAnswer> prQuestions = questionService.getAllPrQuestions(pullRequest.getId());
        List<String> categories = getUniqueCategories(prQuestions);
        return PullRequestDetailReadResponse.from(pullRequest, categories, prQuestions);
    }

    public PullRequestRankingResponses ranking(long userId) {
        AnswerRankings userRanking = answerRankingService.findUserRanking(userId);
        return PullRequestRankingResponses.from(userRanking);
    }

    public void updateToDone(long pullRequestId) {
        pullRequestService.updateToDone(pullRequestId);
    }

    private List<String> getUniqueCategories(List<QuestionAnswer> questionAnswers) {
        return questionAnswers.stream()
                .map(QuestionAnswer::getCategory)
                .distinct()
                .toList();
    }
}
