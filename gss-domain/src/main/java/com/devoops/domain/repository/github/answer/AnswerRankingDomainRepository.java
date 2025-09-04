package com.devoops.domain.repository.github.answer;

import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.answer.AnswerRanking;
import com.devoops.domain.entity.github.answer.AnswerRankings;
import com.devoops.domain.entity.github.pr.PullRequests;

public interface AnswerRankingDomainRepository {

    AnswerRanking save(Answer answer, long userId);

    AnswerRankings findAllByUserId(long userId);

    AnswerRanking update(long pullRequestId, long questionId);

    void deleteById(long id);
}
