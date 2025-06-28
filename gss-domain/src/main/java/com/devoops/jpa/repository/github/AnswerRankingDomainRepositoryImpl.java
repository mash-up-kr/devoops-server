package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.AnswerRanking;
import com.devoops.domain.entity.github.AnswerRankings;
import com.devoops.domain.repository.github.AnswerRankingDomainRepository;
import com.devoops.exception.GssRepositoryException;
import com.devoops.exception.RepositoryErrorCode;
import com.devoops.jpa.entity.github.AnswerRankingEntity;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class AnswerRankingDomainRepositoryImpl implements AnswerRankingDomainRepository {

    private final AnswerRankingJpaRepository answerRankingJpaRepository;

    @Override
    @Transactional
    public AnswerRanking save(AnswerRanking answerRanking) {
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
    public AnswerRanking update(AnswerRanking answerRanking) {
        AnswerRankingEntity answerRankingEntity = answerRankingJpaRepository.findById(answerRanking.getId())
                .orElseThrow(() -> new GssRepositoryException(RepositoryErrorCode.ANSWER_RANKING_NOT_FOUND));
        answerRankingEntity.update(answerRanking);
        return answerRankingEntity.toDomainEntity();
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        answerRankingJpaRepository.deleteById(id);
    }
}
