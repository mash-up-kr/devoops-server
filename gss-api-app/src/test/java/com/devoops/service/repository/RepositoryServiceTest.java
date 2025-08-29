package com.devoops.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
import com.devoops.command.request.RepositoryCreateCommand;
import com.devoops.domain.entity.github.pr.ProcessingStatus;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.repo.GithubRepoDomainRepository;
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


    @Nested
    class Save {

        @Test
        void 신규_레포지토리를_저장할_수_있다() {
            User user = userGenerator.generate("김건우");
            RepositoryCreateCommand createCommand = new RepositoryCreateCommand(user.getId(), "새로운 레포",
                    "url", "건우", 0, 123L);

            GithubRepository savedRepository = repositoryService.save(createCommand);

            assertAll(
                    () -> assertThat(createCommand.url()).isEqualTo(savedRepository.getUrl()),
                    () -> assertThat(createCommand.userId()).isEqualTo(savedRepository.getUserId()),
                    () -> assertThat(createCommand.externalId()).isEqualTo(savedRepository.getExternalId()),
                    () -> assertThat(createCommand.prCount()).isEqualTo(savedRepository.getPrCount()),
                    () -> assertThat(createCommand.ownerName()).isEqualTo(savedRepository.getOwner()),
                    () -> assertThat(createCommand.repositoryName()).isEqualTo(savedRepository.getName())
            );
        }
    }

    @Nested
    class getRepositoryPullRequestsByRepository {

        @Test
        void 레포지토리의_풀_리퀘스트_목록을_머지_최신순으로_가져온다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "김건우의 레포지토리");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(1L));

            List<Long> pullRequestsId = repositoryService.getProcessedPullRequestsByRepository(user, repo.getId(), 6, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertThat(pullRequestsId)
                    .containsExactly(oneMinutesAgoPR.getId(), threeMinutesAgoPR.getId(), fiveMinutesAgoPR.getId());
        }

        @Test
        void 질문_생성이_완료된_풀_리퀘스트만_가져온다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "김건우의 레포지토리");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(5L));
            pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.PROCESSING, repo, now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(1L));

            List<Long> pullRequestsId = repositoryService.getProcessedPullRequestsByRepository(user, repo.getId(), 6, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertThat(pullRequestsId)
                    .containsExactly(oneMinutesAgoPR.getId(), fiveMinutesAgoPR.getId());
        }

        @Test
        void 레포지토리_풀_리퀘스트_목록에_페이지네이션을_적용한다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "김건우의 레포지토리");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(1L));

            List<Long> pageOneIds = repositoryService.getProcessedPullRequestsByRepository(user, repo.getId(), 2, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            List<Long> pageTwoIds = repositoryService.getProcessedPullRequestsByRepository(user, repo.getId(), 2, 1)
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
        void 질문_생성이_완료된_레포지토리_풀_리퀘스트_목록에_페이지네이션을_적용한다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "김건우의 레포지토리");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo, now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.PROCESSING, repo, now.minusMinutes(1L));

            List<Long> pageOneIds = repositoryService.getProcessedPullRequestsByRepository(user, repo.getId(), 1, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            List<Long> pageTwoIds = repositoryService.getProcessedPullRequestsByRepository(user, repo.getId(), 1, 1)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertAll(
                    () -> assertThat(pageOneIds).containsOnly(threeMinutesAgoPR.getId()),
                    () -> assertThat(pageTwoIds).containsOnly(fiveMinutesAgoPR.getId())
            );
        }

        @Test
        void 주인이_아니라면_레포지토리_풀_리퀘스트_목록에_접근할_수_없다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            User seonwoo = userGenerator.generate("선우 누나");
            GithubRepository repo = repoGenerator.generate(user, "김건우의 레포지토리");
            pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING, ProcessingStatus.DONE, repo, now);

            assertThatThrownBy(() -> repositoryService.getProcessedPullRequestsByRepository(seonwoo, repo.getId(), 6, 0))
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
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo1, now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo1, now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo2, now.minusMinutes(1L));
            PullRequest nowPr = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING, ProcessingStatus.DONE, repo2, now);

            List<Long> pullRequestsId = repositoryService.getProcessedPullRequests(user, 4, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertThat(pullRequestsId)
                    .containsExactly(nowPr.getId(), oneMinutesAgoPR.getId(), threeMinutesAgoPR.getId(),
                            fiveMinutesAgoPR.getId());
        }

        @Test
        void 회원의_전체_풀_리퀘스트_목록_중_질문_생성이_완료된_것만_머지_최신순으로_가져온다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo1 = repoGenerator.generate(user, "김건우의 레포지토리1");
            GithubRepository repo2 = repoGenerator.generate(user, "김건우의 레포지토리2");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo1, now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.PROCESSING, repo1, now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo2, now.minusMinutes(1L));
            PullRequest nowPr = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING, ProcessingStatus.DONE, repo2, now);

            List<Long> pullRequestsId = repositoryService.getProcessedPullRequests(user, 4, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertThat(pullRequestsId)
                    .containsExactly(nowPr.getId(), oneMinutesAgoPR.getId(), fiveMinutesAgoPR.getId());
        }

        @Test
        void 회원의_풀_리퀘스트_목록에_페이지네이션을_적용한다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo1 = repoGenerator.generate(user, "김건우의 레포지토리1");
            GithubRepository repo2 = repoGenerator.generate(user, "김건우의 레포지토리2");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo1, now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo1, now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo2, now.minusMinutes(1L));
            PullRequest nowPr = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING, ProcessingStatus.DONE, repo2, now);

            List<Long> pageOneIds = repositoryService.getProcessedPullRequests(user, 2, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            List<Long> pageTwoIds = repositoryService.getProcessedPullRequests(user, 2, 1)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertAll(
                    () -> assertThat(pageOneIds).containsOnly(nowPr.getId(), oneMinutesAgoPR.getId()),
                    () -> assertThat(pageTwoIds).containsOnly(threeMinutesAgoPR.getId(), fiveMinutesAgoPR.getId())
            );
        }

        @Test
        void 질문_생성이_완료된_풀_리퀘스트_목록에_페이지네이션을_적용한다() {
            LocalDateTime now = LocalDateTime.now();
            User user = userGenerator.generate("김건우");
            GithubRepository repo1 = repoGenerator.generate(user, "김건우의 레포지토리1");
            GithubRepository repo2 = repoGenerator.generate(user, "김건우의 레포지토리2");
            PullRequest fiveMinutesAgoPR = pullRequestGenerator.generate("5분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo1, now.minusMinutes(5L));
            PullRequest threeMinutesAgoPR = pullRequestGenerator.generate("3분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo1, now.minusMinutes(3L));
            PullRequest oneMinutesAgoPR = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.DONE, repo2, now.minusMinutes(1L));
            PullRequest nowPr = pullRequestGenerator.generate("1분전 PR", RecordStatus.PENDING,
                    ProcessingStatus.PROCESSING, repo2, now);

            List<Long> pageOneIds = repositoryService.getProcessedPullRequests(user, 2, 0)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            List<Long> pageTwoIds = repositoryService.getProcessedPullRequests(user, 2, 1)
                    .getValues()
                    .stream()
                    .map(PullRequest::getId)
                    .toList();

            assertAll(
                    () -> assertThat(pageOneIds).containsOnly(oneMinutesAgoPR.getId(), threeMinutesAgoPR.getId()),
                    () -> assertThat(pageTwoIds).containsOnly(fiveMinutesAgoPR.getId())
            );
        }
    }



    @Nested
    class StopTracking {

        @Test
        void 레포지토리의_트래킹을_끊을_수_있다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");

            repositoryService.stopTrackingRepository(user, repo.getId());

            GithubRepository foundRepository = githubRepoDomainRepository.findByIdAndUserId(repo.getId(), user.getId());

            assertThat(foundRepository.isTracking()).isFalse();
        }
    }
}
