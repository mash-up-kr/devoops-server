package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.AnswerRanking;
import com.devoops.domain.entity.github.AnswerRankings;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.repository.github.AnswerRankingDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.AnswerRankingEntity;
import com.devoops.jpa.entity.github.PullRequestEntity;
import com.devoops.jpa.entity.github.QuestionEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class AnswerRankingDomainRepositoryImpl implements AnswerRankingDomainRepository {

    private final AnswerRankingJpaRepository answerRankingJpaRepository;
    private final QuestionJpaRepository questionJpaRepository;
    private final PullRequestJpaRepository pullRequestJpaRepository;

    @Override
    @Transactional
    public AnswerRanking save(Answer answer, long userId) {
        QuestionEntity questionEntity = findQuestionById(answer.getQuestionId());
        PullRequestEntity pullRequestEntity = pullRequestJpaRepository.findById(questionEntity.getPullRequestId())
                .orElseThrow(() -> new GssException(ErrorCode.PULL_REQUEST_NOT_FOUND));
        AnswerRanking answerRanking = new AnswerRanking(
                null,
                pullRequestEntity.getId(),
                pullRequestEntity.getTitle(),
                userId,
                answer.getQuestionId(),
                questionEntity.getContent(),
                LocalDateTime.now()
        );

        AnswerRankingEntity answerRankingEntity = new AnswerRankingEntity(answerRanking);
        AnswerRankingEntity savedAnswerRanking = answerRankingJpaRepository.save(answerRankingEntity);
        return savedAnswerRanking.toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public AnswerRankings findAllByUserId(long userId) {
        return answerRankingJpaRepository.findAllByUserId(userId)
                .stream()
                .map(AnswerRankingEntity::toDomainEntity)
                .collect(Collectors.collectingAndThen(Collectors.toList(), AnswerRankings::new));
    }

    @Override
    @Transactional
    public AnswerRanking update(long pullRequestId, long questionId) {
        AnswerRankingEntity answerRankingEntity = answerRankingJpaRepository.findByPullRequestId(pullRequestId)
                .orElseThrow(() -> new GssException(ErrorCode.ANSWER_RANKING_NOT_FOUND));
        QuestionEntity questionEntity = findQuestionById(questionId);
        answerRankingEntity.update(questionEntity);
        return answerRankingEntity.toDomainEntity();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        answerRankingJpaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllInPullRequests(PullRequests pullRequests) {
        List<Long> pullRequestIds = pullRequests.getValues()
                .stream()
                .map(PullRequest::getId)
                .toList();
        answerRankingJpaRepository.deleteByPullRequestIdIn(pullRequestIds);
    }

    private QuestionEntity findQuestionById(long questionId) {
        return questionJpaRepository.findById(questionId)
                .orElseThrow(() -> new GssException(ErrorCode.QUESTION_NOT_FOUND));
    }
}
