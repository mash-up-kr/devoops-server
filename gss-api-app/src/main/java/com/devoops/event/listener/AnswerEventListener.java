package com.devoops.event.listener;

import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.event.UpdateAnswerEvent;
import com.devoops.service.answerranking.AnswerRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AnswerEventListener {

    private final AnswerRankingService answerRankingService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateAnswer(UpdateAnswerEvent updateAnswerEvent) {
        Answer answer = updateAnswerEvent.getAnswer();
        long userId = updateAnswerEvent.getUserId();
        answerRankingService.push(answer, userId);
    }
}
