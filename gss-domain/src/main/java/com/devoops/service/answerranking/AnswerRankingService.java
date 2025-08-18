package com.devoops.service.answerranking;

import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.answer.AnswerRanking;
import com.devoops.domain.entity.github.answer.AnswerRankings;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.domain.repository.github.answer.AnswerRankingDomainRepository;
import com.devoops.domain.repository.github.question.QuestionDomainRepository;
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
        AnswerRankings answerRankings = findUserRanking(userId);
        Question question = questionDomainRepository.findById(answer.getQuestionId());

        Optional<AnswerRanking> samePrRanking = answerRankings.getSamePrRanking(question.getPullRequestId());
        if (samePrRanking.isPresent()) {
            answerRankingDomainRepository.update(samePrRanking.get().getPullRequestId(), answer.getQuestionId());
            return;
        }
        rotateAnswerRanking(answerRankings, answer, userId);
    }

    private void rotateAnswerRanking(AnswerRankings answerRankings, Answer answer, long userId) {
        if (answerRankings.isFull()) {
            AnswerRanking lastUsedRanking = answerRankings.getLastUsedRanking();
            answerRankingDomainRepository.deleteById(lastUsedRanking.getId());
        }
        answerRankingDomainRepository.save(answer, userId);
    }
}
