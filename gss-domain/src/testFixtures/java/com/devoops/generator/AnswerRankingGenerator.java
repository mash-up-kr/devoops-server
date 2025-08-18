package com.devoops.generator;

import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.answer.AnswerRanking;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.jpa.entity.github.answer.AnswerRankingEntity;
import com.devoops.jpa.repository.github.answer.AnswerRankingJpaRepository;
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
