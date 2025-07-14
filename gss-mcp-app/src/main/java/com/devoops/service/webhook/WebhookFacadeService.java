package com.devoops.service.webhook;

import com.devoops.adaptor.GithubAdaptor;
import com.devoops.adaptor.PrAnalysisAdapter;
import com.devoops.client.PrAnalysisClient;
import com.devoops.command.request.PullRequestCreateCommand;
import com.devoops.command.request.QuestionCreateCommand;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.domain.repository.user.UserDomainRepository;
import com.devoops.dto.request.AdaptedAnalyzePrResponse;
import com.devoops.dto.request.GitHubWebhookEventRequest;
import com.devoops.dto.response.AnalyzePrResponse;
import com.devoops.util.SummaryFormatter;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookFacadeService {

    private final GithubAdaptor githubAdaptor;
    private final PrAnalysisClient prAnalysisClient;
    private final PrAnalysisAdapter prAnalysisAdapter;
    private final UserDomainRepository userDomainRepository;
    private final QuestionDomainRepository questionDomainRepository;
    private final GithubRepoDomainRepository githubRepoDomainRepository;
    private final PullRequestDomainRepository pullRequestDomainRepository;

    public void createQuestionWithWebhookEvent(GitHubWebhookEventRequest gitHubWebhookEventRequest) {
        if (!gitHubWebhookEventRequest.isMerged()) {
            return;
        }

        User triggerUser = userDomainRepository.findByProviderId(gitHubWebhookEventRequest.getUserId());
        GithubToken githubToken = triggerUser.getGithubToken();

        // take diff code
        String diff = githubAdaptor.getCodeChangeHistory(gitHubWebhookEventRequest.getPullRequestDiffUrl(),
                githubToken.getToken());

        // 분석 결과
        AdaptedAnalyzePrResponse adaptedAnalyzePrResponse = prAnalysisAdapter.analyze(
                gitHubWebhookEventRequest.getTitle(),
                gitHubWebhookEventRequest.getDescription(),
                diff
        );

        // 레포 아이디를 기반으로 찾기 -> 풀리퀘 생성 -> prCount 올리기
        GithubRepository githubRepository = githubRepoDomainRepository.findByExternalId(gitHubWebhookEventRequest.getRepositoryId());

        PullRequest readyPullRequest = pullRequestDomainRepository.save(
                new PullRequestCreateCommand(
                        githubRepository.getId(),
                        triggerUser.getId(),
                        gitHubWebhookEventRequest.getTitle(),
                        gitHubWebhookEventRequest.getDescription(),
                        adaptedAnalyzePrResponse.summary(),
                        adaptedAnalyzePrResponse.detailSummary(),
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
        PullRequest updatedPullRequest = pullRequestDomainRepository.updateAnalyzedResult(readyPullRequest.getId(),
                adaptedAnalyzePrResponse.summary());

        questionDomainRepository.saveAll(
                createQuestionListFromCategorizedQuestions(adaptedAnalyzePrResponse.questions(), updatedPullRequest.getId())
        );
    }

    private List<Question> createQuestionListFromCategorizedQuestions(
            List<AnalyzePrResponse.CategorizedQuestion> questions, Long pulLRequestId) {
        return questions.stream().flatMap(
                        taggedQuestion -> taggedQuestion.question()
                                .stream()
                                .map(
                                        question -> new QuestionCreateCommand(pulLRequestId, taggedQuestion.category(),
                                                question)
                                )
                )
                .map(QuestionCreateCommand::toDomainEntity).toList();
    }
}
