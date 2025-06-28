package com.devoops.service.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.PullRequestReadResponse;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class PullRequestFacadeServiceTest extends BaseServiceTest {

    @Autowired
    private PullRequestFacadeService pullRequestFacadeService;

    @Nested
    class Read {

        @Test
        void 풀_리퀘스트_정보를_읽는다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PENDING, repo, LocalDateTime.now());
            Question question1 = questionGenerator.generate(pullRequest, "질문1");
            Question question2 = questionGenerator.generate(pullRequest, "질문2");
            Answer answer1 = answerGenerator.generate(question1, "answerContent");

            PullRequestReadResponse response = pullRequestFacadeService.read(pullRequest.getId());

            assertAll(
                    () -> assertThat(response.id()).isEqualTo(pullRequest.getId()),
                    () -> assertThat(response.questions()).hasSize(2),
                    () -> assertThat(response.questions().get(0).createdAt()).isNotNull(),
                    () -> assertThat(response.questions().get(1).createdAt()).isNull()
            );
        }
    }
}
