package com.devoops.service.facade;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.AnswerRanking;
import com.devoops.domain.entity.github.Answers;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.AnswerPutRequests;
import com.devoops.service.answerranking.AnswerRankingService;
import com.devoops.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionFacadeService {

    private final QuestionService questionService;
    private final AnswerRankingService answerRankingService;

    public Answer initializeAnswer(long questionId, User user) {
        return questionService.initializeAnswer(questionId, user);
    }

    public Answer updateAnswer(long answerId, String updateContent, long userId) {
        Answer answer = questionService.updateAnswer(answerId, updateContent);
        answerRankingService.push(answer, userId);
        return answer;
    }

    public Answers updateAllAnswers(AnswerPutRequests updateRequests) {
        return questionService.updateAllAnswers(updateRequests);
    }

    public void deleteAnswer(long answerId) {
        questionService.deleteAnswer(answerId);
    }

}
