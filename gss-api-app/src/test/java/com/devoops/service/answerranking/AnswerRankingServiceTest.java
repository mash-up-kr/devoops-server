package com.devoops.service.answerranking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.AnswerRanking;
import com.devoops.domain.entity.github.AnswerRankings;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.AnswerRankingDomainRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AnswerRankingServiceTest extends BaseServiceTest {

    @Autowired
    private AnswerRankingService answerRankingService;

    @Autowired
    private AnswerRankingDomainRepository answerRankingDomainRepository;


    @Nested
    class Push {

        @Test
        void PR_랭킹을_저장한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pullRequest = pullRequestGenerator.generate("최초 PR", RecordStatus.PENDING, repo,
                    LocalDateTime.now());
            Question question1 = questionGenerator.generate(pullRequest, "질문1");
            Answer answer = answerGenerator.generate(question1, "answer1");

            answerRankingService.push(answer, user.getId());

            AnswerRankings userRanking = answerRankingDomainRepository.findAllByUserId(user.getId());
            assertThat(userRanking.getRankings()).hasSize(1);
        }

        @Test
        void 유저의_PR_랭킹을_모두_가져온다() {
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

            List<Long> userRankingIds = answerRankingService.findUserRanking(user.getId())
                    .getRankings()
                    .stream()
                    .map(AnswerRanking::getId)
                    .toList();

            assertThat(userRankingIds).containsExactly(answerRanking1.getId(), answerRanking2.getId());
        }

        @Test
        void 랭킹_PR에_존재하는_질문의_경우_랭킹내역을_업데이트한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pr1 = pullRequestGenerator.generate("PR1", RecordStatus.PENDING, repo, LocalDateTime.now());
            Question question1 = questionGenerator.generate(pr1, "질문1");
            Question question2 = questionGenerator.generate(pr1, "질문2");
            Answer answer1 = answerGenerator.generate(question1, "answer1");
            Answer answer2 = answerGenerator.generate(question2, "answer2");
            answerRankingGenerator.generate(pr1, question1, answer1, user.getId());

            answerRankingService.push(answer2, user.getId());

            AnswerRankings userRanking = answerRankingDomainRepository.findAllByUserId(user.getId());
            assertAll(
                    () -> assertThat(userRanking.getRankings()).hasSize(1),
                    () -> assertThat(userRanking.getRankings().get(0).getQuestionContent()).isEqualTo(
                            question2.getContent())
            );
        }

        @Test
        void 랭킹_PR이_꽉_찼을_경우_가장_오래된_pr을_삭제하고_새로운_pr을_갱신한다() {
            User user = userGenerator.generate("김건우");
            GithubRepository repo = repoGenerator.generate(user, "건우의 레포");
            PullRequest pr1 = pullRequestGenerator.generate("PR1", RecordStatus.PENDING, repo, LocalDateTime.now());
            PullRequest pr2 = pullRequestGenerator.generate("PR2", RecordStatus.PENDING, repo, LocalDateTime.now());
            PullRequest pr3 = pullRequestGenerator.generate("PR3", RecordStatus.PENDING, repo, LocalDateTime.now());
            PullRequest pr4 = pullRequestGenerator.generate("PR4", RecordStatus.PENDING, repo, LocalDateTime.now());
            Question question1 = questionGenerator.generate(pr1, "질문1");
            Question question2 = questionGenerator.generate(pr2, "질문2");
            Question question3 = questionGenerator.generate(pr3, "질문3");
            Question question4 = questionGenerator.generate(pr4, "질문4");
            Answer answer1 = answerGenerator.generate(question1, "answer1");
            Answer answer2 = answerGenerator.generate(question2, "answer2");
            Answer answer3 = answerGenerator.generate(question3, "answer2");
            Answer answer4 = answerGenerator.generate(question4, "answer2");
            answerRankingGenerator.generate(pr1, question1, answer1, user.getId());
            answerRankingGenerator.generate(pr2, question2, answer2, user.getId());
            answerRankingGenerator.generate(pr3, question3, answer3, user.getId());

            answerRankingService.push(answer4, user.getId());

            List<Long> userRankingPullRequestIds = answerRankingDomainRepository.findAllByUserId(user.getId())
                    .getRankings()
                    .stream()
                    .map(AnswerRanking::getPullRequestId)
                    .toList();

            assertAll(
                    () -> assertThat(userRankingPullRequestIds).hasSize(3),
                    () -> assertThat(userRankingPullRequestIds).containsExactly(pr2.getId(), pr3.getId(), pr4.getId())
            );
        }
    }
}
