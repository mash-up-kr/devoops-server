package com.devoops.event;

import com.devoops.adaptor.GithubAdaptor;
import com.devoops.adaptor.PrAnalysisAdapter;
import com.devoops.command.request.QuestionCreateCommand;
import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import com.devoops.dto.response.PrAnalysis;
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

    private final GithubAdaptor githubAdaptor;
    private final PrAnalysisAdapter prAnalysisAdapter;
    private final PullRequestService pullRequestService;
    private final QuestionService questionService;

    @Async
    @EventListener(QuestionCreateEvent.class)
    public void createQuestion(QuestionCreateEvent questionCreateEvent) {
        AppWebhookEventRequest request = questionCreateEvent.getRequest();
        GithubToken githubToken = questionCreateEvent.getToken();
        PullRequest readyPullRequest = questionCreateEvent.getInitializedPullRequest();

        String diff = githubAdaptor.getCodeChangeHistory(request.diffUrl(), githubToken.getToken());
        AdaptedAnalyzePrResponse adaptedAnalyzePrResponse = prAnalysisAdapter.analyze(request.title(), request.description(), diff, "gpt-5-mini");


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
