package com.devoops.service.webhook;

import com.devoops.adaptor.GithubAdaptor;
import com.devoops.client.PrAnalysisClient;
import com.devoops.command.request.PullRequestCreateCommand;
import com.devoops.command.request.QuestionCreateCommand;
import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.domain.repository.user.UserDomainRepository;
import com.devoops.dto.request.GitHubWebhookEventRequest;
import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.util.SummaryFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebhookFacadeService {

    private final GithubAdaptor githubAdaptor;
    private final PrAnalysisClient prAnalysisClient;
    private final UserDomainRepository userDomainRepository;
    private final QuestionDomainRepository questionDomainRepository;
    private final PullRequestDomainRepository pullRequestDomainRepository;
    private final GithubTokenDomainRepository githubTokenDomainRepository;

    public void createQuestionWithWebhookEvent(GitHubWebhookEventRequest gitHubWebhookEventRequest) {
        if (!isPullRequestMergedEvent(gitHubWebhookEventRequest)) return;

        /**
         * FIXME
         * 1. 예전에 이야기했던대로 ErrorCode를 Common으로 내리면 해결
         * 현재 api 모듈에 에러코드가 정의되어 있어서 token domain level의 errorCode를 mcp module에서 사용 불가
         *
         * 2. eventRequest로 내려오는 userId가 우리 userDomain의 id가 맞는지 확인 필요
         */
        User triggerUser = userDomainRepository.findById(gitHubWebhookEventRequest.getUserId());
        GithubToken githubToken = githubTokenDomainRepository.findByUserId(triggerUser)
            .orElseThrow(() -> new RuntimeException("ErrorCode.NO_RESOURCE_FOUND"));
//                .orElseThrow(() -> new GssException(ErrorCode.NO_RESOURCE_FOUND));

        // take diff code
        String diff = githubAdaptor.getCodeChangeHistory(gitHubWebhookEventRequest.getPullRequestDiffUrl(), githubToken.getToken());

        // 분석 결과
        AnalyzePrResponse analyzePrResponse = prAnalysisClient.analyze(
            gitHubWebhookEventRequest.getTitle(),
            gitHubWebhookEventRequest.getDescription(),
            diff
        );

        List<Map.Entry<String, String>> converted = analyzePrResponse.summaryDetails().stream()
            .map(sd -> Map.entry(sd.title(), sd.description()))
            .toList();

        // pull request ready
        PullRequest readyPullRequest = pullRequestDomainRepository.save(
            new PullRequestCreateCommand(
                gitHubWebhookEventRequest.getRepositoryId(),
                gitHubWebhookEventRequest.getUserId(),
                gitHubWebhookEventRequest.getTitle(),
                gitHubWebhookEventRequest.getDescription(),
                analyzePrResponse.summary(),
                SummaryFormatter.formatWithNumbering(converted),
                gitHubWebhookEventRequest.getExternalId(),
                RecordStatus.PENDING,
                gitHubWebhookEventRequest.getMergedAt(),
                gitHubWebhookEventRequest.getTag()
            ).toDomainEntity()
        );

        /**
         * FIXME
         * 현재 appService에서 client 통신이 포함되어 있어 트랜잭션을 걸지 못함.
         * 설계대로 각 하위 서비스에서 트랜잭션을 분리해서 최소한으로 가져가기
         * 이 로직에서 pullRequest create와 List<Question> create는 하나의 트랜잭션으로 보장되어야하지 않을까라는 생각
         * -> 그렇다면 하위 서비스에서 createCommand가 묶여야 함 (question domain은 항상 pull request create와 연결)
         */
        PullRequest updatedPullRequest = pullRequestDomainRepository.updateAnalyzedResult(readyPullRequest.getId(), analyzePrResponse.summary());

        questionDomainRepository.saveAll(
            createQuestionListFromCategorizedQuestions(analyzePrResponse.questions(), updatedPullRequest.getId())
        );
    }

    private boolean isPullRequestMergedEvent(GitHubWebhookEventRequest gitHubWebhookEventRequest) {
        return gitHubWebhookEventRequest.isMerged();
    }

    private List<Question> createQuestionListFromCategorizedQuestions(List<AnalyzePrResponse.CategorizedQuestion> questions, Long pulLRequestId) {
        return questions.stream().flatMap(
                taggedQuestion -> taggedQuestion.question()
                    .stream()
                    .map(
                        question -> new QuestionCreateCommand(pulLRequestId, taggedQuestion.category(), question)
                    )
            )
            .map(QuestionCreateCommand::toDomainEntity).toList();
    }
}
