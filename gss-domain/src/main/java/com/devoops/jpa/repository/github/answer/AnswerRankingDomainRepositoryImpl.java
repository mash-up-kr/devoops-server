package com.devoops.jpa.repository.github.answer;

import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.answer.AnswerRanking;
import com.devoops.domain.entity.github.answer.AnswerRankings;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.pr.PullRequests;
import com.devoops.domain.repository.github.answer.AnswerRankingDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.answer.AnswerRankingEntity;
import com.devoops.jpa.entity.github.pr.PullRequestEntity;
import com.devoops.jpa.entity.github.question.QuestionEntity;
import com.devoops.jpa.repository.github.pr.PullRequestJpaRepository;
import com.devoops.jpa.repository.github.question.QuestionJpaRepository;
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

    private QuestionEntity findQuestionById(long questionId) {
        return questionJpaRepository.findById(questionId)
                .orElseThrow(() -> new GssException(ErrorCode.QUESTION_NOT_FOUND));
    }
}
