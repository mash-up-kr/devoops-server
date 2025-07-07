package com.devoops.generator;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.AnswerRanking;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.Question;
import com.devoops.jpa.entity.github.AnswerRankingEntity;
import com.devoops.jpa.repository.github.AnswerRankingJpaRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnswerRankingGenerator {

    @Autowired
    private AnswerRankingJpaRepository answerRankingJpaRepository;

    public AnswerRanking generate(PullRequest pullRequest, Question question, Answer answer, long userId) {
        AnswerRanking answerRanking = new AnswerRanking(
                null,
                pullRequest.getId(),
                pullRequest.getTitle(),
                userId,
                answer.getQuestionId(),
                question.getContent(),
                LocalDateTime.now()
        );

        AnswerRankingEntity answerRankingEntity = new AnswerRankingEntity(answerRanking);
        AnswerRankingEntity savedAnswerRanking = answerRankingJpaRepository.save(answerRankingEntity);
        return savedAnswerRanking.toDomainEntity();
    }
}
