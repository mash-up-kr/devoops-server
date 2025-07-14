package com.devoops.service.facade;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.entity.github.Answers;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.AnswerPutRequests;
import com.devoops.service.answer.AnswerService;
import com.devoops.service.answerranking.AnswerRankingService;
import com.devoops.service.pullrequest.PullRequestService;
import com.devoops.service.question.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionFacadeService {

    private final PullRequestService pullRequestService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final AnswerRankingService answerRankingService;

    public Answer initializeAnswer(long questionId, User user) {
        PullRequest pullRequest = pullRequestService.findByQuestionId(questionId);
        if (pullRequest.isPending()) {
            pullRequestService.updateStatus(pullRequest.getId(), RecordStatus.PROGRESS);
        }
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
        Answer answer = answerService.findById(answerId);
        PullRequest pullRequest = pullRequestService.findByQuestionId(answer.getQuestionId());
        long answerCount = answerService.getAnswerCountByPullRequestId(pullRequest.getId());
        if(answerCount == 1) {
            pullRequestService.updateStatus(pullRequest.getId(), RecordStatus.PENDING);
        }
        questionService.deleteAnswer(answerId);
    }
}
