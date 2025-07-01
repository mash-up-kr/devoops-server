package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.AnswerRanking;
import com.devoops.domain.entity.github.AnswerRankings;

public interface AnswerRankingDomainRepository {

    AnswerRanking save(Answer answer, long userId);

    AnswerRankings findAllByUserId(long userId);

    AnswerRanking update(AnswerRanking answerRanking);

    void deleteById(long id);
}
