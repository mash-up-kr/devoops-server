package com.devoops.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
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
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RepositoryServiceTest extends BaseServiceTest {

    @Autowired
    private RepositoryService repositoryService;

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

    @Nested
    class getRepositoryPullRequestsByRepository {

        @Test
        void 레포지토리의_풀_리퀘스트_목록을_머지_최신순으로_가져온다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "김건우의 레포지토리");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING, repo,
                    now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING, repo,
                    now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING, repo,
                    now.minusMinutes(1L));

            List<Long> pullRequestsId = repositoryService.getPullRequestsByRepository(user, repo.getId(), 6, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertThat(pullRequestsId)
                    .containsExactly(oneMinutesAgoPR.getId(), threeMinutesAgoPR.getId(), fiveMinutesAgoPR.getId());
        }

        @Test
        void 레포지토리_풀_리퀘스트_목록에_페이지네이션을_적용한다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "김건우의 레포지토리");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING, repo,
                    now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING, repo,
                    now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING, repo,
                    now.minusMinutes(1L));

            List<Long> pageOneIds = repositoryService.getPullRequestsByRepository(user, repo.getId(), 2, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            List<Long> pageTwoIds = repositoryService.getPullRequestsByRepository(user, repo.getId(), 2, 1)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertAll(
                    () -> assertThat(pageOneIds).containsOnly(oneMinutesAgoPR.getId(), threeMinutesAgoPR.getId()),
                    () -> assertThat(pageTwoIds).containsOnly(fiveMinutesAgoPR.getId())
            );
        }

        @Test
        void 주인이_아니라면_레포지토리_풀_리퀘스트_목록에_접근할_수_없다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            User seonwoo = userGenerator.generate("선우 누나");
            GithubRepository repo = repoGenerator.generate(user, "김건우의 레포지토리");
            pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING, repo, now);

            assertThatThrownBy(() -> repositoryService.getPullRequestsByRepository(seonwoo, repo.getId(), 6, 0))
                    .isInstanceOf(GssException.class)
                    .hasMessage(ErrorCode.REPOSITORY_NOT_FOUND.getMessage());
        }
    }

    @Nested
    class getRepositoryPullRequests {

        @Test
        void 회원의_전체_풀_리퀘스트_목록을_머지_최신순으로_가져온다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo1 = repoGenerator.generate(user, "김건우의 레포지토리1");
            GithubRepository repo2 = repoGenerator.generate(user, "김건우의 레포지토리2");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING, repo1,
                    now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING, repo1,
                    now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING, repo2,
                    now.minusMinutes(1L));
            PullRequest nowPr = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING, repo2, now);

            List<Long> pullRequestsId = repositoryService.getPullRequests(user, 4, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertThat(pullRequestsId)
                    .containsExactly(nowPr.getId(), oneMinutesAgoPR.getId(), threeMinutesAgoPR.getId(), fiveMinutesAgoPR.getId());
        }

        @Test
        void 회원의_풀_리퀘스트_목록에_페이지네이션을_적용한다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo1 = repoGenerator.generate(user, "김건우의 레포지토리1");
            GithubRepository repo2 = repoGenerator.generate(user, "김건우의 레포지토리2");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING, repo1,
                    now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING, repo1,
                    now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING, repo2,
                    now.minusMinutes(1L));
            PullRequest nowPr = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING, repo2, now);

            List<Long> pageOneIds = repositoryService.getPullRequests(user, 2, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            List<Long> pageTwoIds = repositoryService.getPullRequests(user, 2, 1)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertAll(
                    () -> assertThat(pageOneIds).containsOnly(nowPr.getId(), oneMinutesAgoPR.getId()),
                    () -> assertThat(pageTwoIds).containsOnly(threeMinutesAgoPR.getId(), fiveMinutesAgoPR.getId())
            );
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

            repositoryService.delete(user, repo.getId());

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
