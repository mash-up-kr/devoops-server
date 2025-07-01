package com.devoops.service.facade;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.Answers;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.AnswerPutRequests;
import com.devoops.service.question.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionFacadeService {

    private final QuestionService questionService;

    public Answer initializeAnswer(long questionId, User user) {
        return questionService.initializeAnswer(questionId, user);
    }

    public List<QuestionAnswer> getAllPrQuestions(long pullRequestsId) {
        return questionService.getAllPrQuestions(pullRequestsId);
    }

    public Answer updateAnswer(long answerId, String updateContent) {
        return questionService.updateAnswer(answerId, updateContent);
    }

    public Answers updateAllAnswers(AnswerPutRequests updateRequests) {
        return questionService.updateAllAnswers(updateRequests);
    }

    public void deleteAnswer(long answerId) {
        questionService.deleteAnswer(answerId);
    }

}
