package com.devoops.service.answerranking;

import com.devoops.domain.entity.github.AnswerRankings;
import com.devoops.domain.repository.github.AnswerRankingDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerRankingService {

    private final AnswerRankingDomainRepository answerRankingDomainRepository;

    public AnswerRankings findUserRanking(long userId) {
        return answerRankingDomainRepository.findAllByUserId(userId);
    }
}
