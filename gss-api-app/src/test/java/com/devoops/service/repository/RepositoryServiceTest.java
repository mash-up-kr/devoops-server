package com.devoops.service.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.entity.user.User;
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

    @Nested
    class getRepositoryPullRequests {

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
                    .hasMessage(ErrorCode.REPOSITIORY_NOT_FOUND.getMessage());
        }


    }
}
