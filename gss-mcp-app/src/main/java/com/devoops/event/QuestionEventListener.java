package com.devoops.event;

import com.devoops.command.request.QuestionCreateCommand;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import com.devoops.dto.response.PrAnalysis;
import com.devoops.service.pranalysis.PrAnalysisService;
import com.devoops.service.pullrequest.PullRequestService;
import com.devoops.service.question.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionEventListener {

    private final PullRequestService pullRequestService;
    private final QuestionService questionService;
    private final PrAnalysisService prAnalysisService;

    @Async
    @EventListener(QuestionCreateEvent.class)
    public void createQuestion(QuestionCreateEvent questionCreateEvent) {
        AppWebhookEventRequest request = questionCreateEvent.getRequest();
        GithubToken githubToken = questionCreateEvent.getToken();
        PullRequest readyPullRequest = questionCreateEvent.getInitializedPullRequest();

        AdaptedAnalyzePrResponse adaptedAnalyzePrResponse = prAnalysisService.analyzePullRequest(request, githubToken);

        PullRequest updatedPullRequest = pullRequestService.updateAnalyzeResult(
                readyPullRequest.getId(),
                adaptedAnalyzePrResponse.summary(),
                adaptedAnalyzePrResponse.detailSummary()
        );

        List<Question> createdQuestions = createQuestionListFromCategorizedQuestions(
                adaptedAnalyzePrResponse.questions(), updatedPullRequest.getId()
        );
        questionService.saveAll(createdQuestions, updatedPullRequest.getId());

    }

    private List<Question> createQuestionListFromCategorizedQuestions(
            List<PrAnalysis.CategorizedQuestion> questions,
            Long pulLRequestId
    ) {
        return questions.stream()
                .flatMap(taggedQuestion -> taggedQuestion.question()
                        .stream()
                        .map(question -> new QuestionCreateCommand(pulLRequestId, taggedQuestion.category(), question))
                )
                .map(QuestionCreateCommand::toDomainEntity)
                .toList();
    }
}
