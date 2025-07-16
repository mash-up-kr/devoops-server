package com.devoops.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

import com.devoops.BaseServiceTest;
import com.devoops.client.GitHubClient;
import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.AnswerRanking;
import com.devoops.domain.entity.github.AnswerRankings;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.AnswerDomainRepository;
import com.devoops.domain.repository.github.AnswerRankingDomainRepository;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.dto.request.RepositorySaveRequest;
import com.devoops.dto.response.GithubRepoInfoResponse;
import com.devoops.dto.response.OwnerResponse;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.generator.AnswerRankingGenerator;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class RepositoryFacadeServiceTest extends BaseServiceTest {

    @Autowired
    private GithubRepoDomainRepository githubRepoDomainRepository;

    @Autowired
    private PullRequestDomainRepository pullRequestDomainRepository;

    @Autowired
    private QuestionDomainRepository questionDomainRepository;

    @Autowired
    private AnswerRankingDomainRepository answerRankingDomainRepository;

    @Autowired
    private AnswerDomainRepository answerDomainRepository;

    @Autowired
    private RepositoryFacadeService repositoryFacadeService;

    @MockitoBean
    private GitHubClient gitHubClient;

    @Nested
    class Save {

        @Test
        void 레포지토리를_저장하고_웹훅을_심는다() {
            User user = userGenerator.generate("김건우");
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");
            GithubRepoInfoResponse mockResponse = new GithubRepoInfoResponse(123, "testName", "testUrl",
                    new OwnerResponse("김건우"));
            Mockito.when(gitHubClient.getRepositoryInfo(anyString(), anyString(), anyString()))
                    .thenReturn(mockResponse);

            GithubRepository savedRepository = repositoryFacadeService.save(request, user);

            boolean exists = githubRepoDomainRepository.existsByIdAndUserId(savedRepository.getId(), user.getId());

            assertAll(
                    () -> assertThat(exists).isTrue(),
                    () -> Mockito.verify(gitHubClient, times(1)).createWebhook(any(), any(), any(), any()),
                    () -> Mockito.verify(gitHubClient, times(1)).getRepositoryInfo(any(), any(), any())
            );
        }

        @Test
        void 레포지토리를_중복_저장할_수_없다() {
            User user = userGenerator.generate("김건우");
            RepositorySaveRequest request = new RepositorySaveRequest("https://github.com/octocat/Hello-World");
            GithubRepoInfoResponse mockResponse = new GithubRepoInfoResponse(123, "testName", "testUrl",
                    new OwnerResponse("김건우"));
            Mockito.when(gitHubClient.getRepositoryInfo(anyString(), anyString(), anyString()))
                    .thenReturn(mockResponse);
            repositoryFacadeService.save(request, user);

            assertThatThrownBy(() -> repositoryFacadeService.save(request, user))
                    .isInstanceOf(GssException.class)
                    .hasMessage(ErrorCode.ALREADY_SAVED_REPOSITORY.getMessage());
        }
    }

    @Nested
    class Delete {

        @Test
        void 레포지토리를_삭제할_수_있다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pr1 = pullRequestGenerator.generate("PR1", RecordStatus.PENDING, repo, LocalDateTime.now());
            PullRequest pr2 = pullRequestGenerator.generate("PR2", RecordStatus.PENDING, repo, LocalDateTime.now());
            Question question1 = questionGenerator.generate(pr1, "질문1");
            Question question2 = questionGenerator.generate(pr2, "질문2");
            Answer answer1 = answerGenerator.generate(question1, "answer1");
            Answer answer2 = answerGenerator.generate(question2, "answer2");
            AnswerRanking answerRanking1 = answerRankingGenerator.generate(pr1, question1, answer1, user.getId());
            AnswerRanking answerRanking2 = answerRankingGenerator.generate(pr2, question2, answer2, user.getId());

            repositoryFacadeService.deleteRepository(user, repo.getId());

            boolean exists = githubRepoDomainRepository.existsByIdAndUserId(repo.getId(), user.getId());
            PullRequests repositoryPrs = pullRequestDomainRepository.findByRepositoryId(repo.getId());
            List<QuestionAnswer> pr1Questions = questionDomainRepository.findAllPrQuestions(pr1.getId());
            List<QuestionAnswer> pr2Questions = questionDomainRepository.findAllPrQuestions(pr2.getId());
            AnswerRankings userAnswerRankings = answerRankingDomainRepository.findAllByUserId(user.getId());
            long pr1AnswerCount = answerDomainRepository.getAnswerCountByPullRequestId(pr1.getId());
            long pr2AnswerCount = answerDomainRepository.getAnswerCountByPullRequestId(pr2.getId());

            assertAll(
                    () -> assertThat(exists).isFalse(),
                    () -> assertThat(repositoryPrs.getValues()).isEmpty(),
                    () -> assertThat(pr1Questions).isEmpty(),
                    () -> assertThat(pr2Questions).isEmpty(),
                    () -> assertThat(userAnswerRankings.getRankings()).isEmpty(),
                    () -> assertThat(pr1AnswerCount).isZero(),
                    () -> assertThat(pr2AnswerCount).isZero()
            );
        }
    }
}
