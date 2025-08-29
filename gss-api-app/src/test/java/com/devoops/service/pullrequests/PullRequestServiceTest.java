package com.devoops.service.pullrequests;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.github.pr.ProcessingStatus;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.service.pullrequest.PullRequestService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PullRequestServiceTest extends BaseServiceTest {

    @Autowired
    private PullRequestService pullRequestService;

    @Nested
    class UpdateToDone {

        @Test
        void 풀_리퀘스트_회고_상태를_완료로_변경한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate(
                    "최초 PR",
                    RecordStatus.PENDING,
                    ProcessingStatus.DONE,
                    repo,
                    LocalDateTime.now()
            );

            PullRequest updatedPullRequest = pullRequestService.updateStatus(pullRequest.getId(), RecordStatus.DONE);

            assertThat(updatedPullRequest.getRecordStatus()).isEqualTo(RecordStatus.DONE);
        }
    }
}
