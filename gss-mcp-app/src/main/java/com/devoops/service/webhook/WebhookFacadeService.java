package com.devoops.service.webhook;

import com.devoops.adaptor.GithubAdaptor;
import com.devoops.adaptor.PrAnalysisAdapter;
import com.devoops.command.request.PullRequestCreateCommand;
import com.devoops.command.request.QuestionCreateCommand;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.domain.repository.user.UserDomainRepository;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import com.devoops.dto.AppWebhookEventRequest;
import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.service.pullrequest.PullRequestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookFacadeService {

    private final GithubAdaptor githubAdaptor;
    private final PrAnalysisAdapter prAnalysisAdapter;
    private final UserDomainRepository userDomainRepository;
    private final QuestionDomainRepository questionDomainRepository;
    private final GithubRepoDomainRepository githubRepoDomainRepository;
    private final PullRequestDomainRepository pullRequestDomainRepository;
    private final PullRequestService pullRequestService;

    public void createQuestionWithWebhookEvent(AppWebhookEventRequest request) {
        if (!request.isMerged()) {
            return;
        }

        User triggerUser = userDomainRepository.findByProviderId(request.userId());
        GithubToken githubToken = triggerUser.getGithubToken();

        // take diff code -> 분석
        String diff = githubAdaptor.getCodeChangeHistory(request.diffUrl(), githubToken.getToken());
        AdaptedAnalyzePrResponse adaptedAnalyzePrResponse = prAnalysisAdapter.analyze(request.title(), request.description(), diff);

        // 레포 아이디를 기반으로 찾기 -> 풀리퀘 생성 -> prCount 올리기
        PullRequest readyPullRequest = savePullRequest(
                triggerUser.getId(),
                request,
                adaptedAnalyzePrResponse
        );

        /**
         * FIXME
         * 현재 appService에서 client 통신이 포함되어 있어 트랜잭션을 걸지 못함.
         * 설계대로 각 하위 서비스에서 트랜잭션을 분리해서 최소한으로 가져가기
         * 이 로직에서 pullRequest create와 List<Question> create는 하나의 트랜잭션으로 보장되어야하지 않을까라는 생각
         * -> 그렇다면 하위 서비스에서 createCommand가 묶여야 함 (question domain은 항상 pull request create와 연결)
         */
        PullRequest updatedPullRequest = pullRequestDomainRepository.updateAnalyzedResult(
                readyPullRequest.getId(),
                adaptedAnalyzePrResponse.summary(),
                adaptedAnalyzePrResponse.detailSummary()
        );

        List<Question> createdQuestions = createQuestionListFromCategorizedQuestions(
                adaptedAnalyzePrResponse.questions(), updatedPullRequest.getId()
        );
        questionDomainRepository.saveAll(createdQuestions, updatedPullRequest.getId());
    }

    private PullRequest savePullRequest(
            long userId,
            AppWebhookEventRequest request,
            AdaptedAnalyzePrResponse prAnalyzeResponse
    ) {
        GithubRepository githubRepository = githubRepoDomainRepository.findByExternalId(request.repositoryId());
        PullRequestCreateCommand prCreateCommand = resolvePRCreateCommand(request, githubRepository.getId(), userId, prAnalyzeResponse);
        return pullRequestService.save(prCreateCommand);
    }

    private PullRequestCreateCommand resolvePRCreateCommand(
            AppWebhookEventRequest appWebhookEventRequest,
            long repositoryId,
            long userId,
            AdaptedAnalyzePrResponse adaptedAnalyzePrResponse
    ) {
        return new PullRequestCreateCommand(
                repositoryId,
                userId,
                appWebhookEventRequest.title(),
                appWebhookEventRequest.description(),
                adaptedAnalyzePrResponse.summary(),
                adaptedAnalyzePrResponse.detailSummary(),
                appWebhookEventRequest.getParsedUrl().getValue(),
                appWebhookEventRequest.pullRequestId(),
                appWebhookEventRequest.label(),
                appWebhookEventRequest.mergedAt()
        );
    }

    private List<Question> createQuestionListFromCategorizedQuestions(
            List<AnalyzePrResponse.CategorizedQuestion> questions,
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
