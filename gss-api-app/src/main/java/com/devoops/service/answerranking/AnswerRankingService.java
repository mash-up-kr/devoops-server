package com.devoops.service.answerranking;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.AnswerRanking;
import com.devoops.domain.entity.github.AnswerRankings;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.repository.github.AnswerRankingDomainRepository;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerRankingService {

    private final AnswerRankingDomainRepository answerRankingDomainRepository;
    private final QuestionDomainRepository questionDomainRepository;

    public AnswerRankings findUserRanking(long userId) {
        return answerRankingDomainRepository.findAllByUserId(userId);
    }

    public void push(Answer answer, long userId) {
        //유저별 랭킹 가져오기
        AnswerRankings answerRankings = findUserRanking(userId);
        Question question = questionDomainRepository.findById(answer.getQuestionId());

        Optional<AnswerRanking> samePrRanking = answerRankings.getSamePrRanking(question.getPullRequestId());
        //같은 PR이 있다면 덮어쓰기
        if(samePrRanking.isPresent()) {
            AnswerRanking answerRanking = samePrRanking.get();
            answerRankingDomainRepository.update(answerRanking);
            return;
        }

        //다 찾다면 가장 느린 것 삭제
        if(answerRankings.isFull()) {
            AnswerRanking lastUsedRanking = answerRankings.getLastUsedRanking();
            answerRankingDomainRepository.deleteById(lastUsedRanking.getId());
        }

        //새로운 answerRanking 추가하기
        answerRankingDomainRepository.save(answer, userId);
    }
}
