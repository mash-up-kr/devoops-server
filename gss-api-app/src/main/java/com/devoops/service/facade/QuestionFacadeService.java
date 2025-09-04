package com.devoops.service.facade;

import com.devoops.command.request.AnswerUpdateCommand;
import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.answer.Answers;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.AnswerPutRequests;
import com.devoops.event.UpdateAnswerEvent;
import com.devoops.service.answer.AnswerService;
import com.devoops.service.answerranking.AnswerRankingService;
import com.devoops.service.pullrequest.PullRequestService;
import com.devoops.service.question.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuestionFacadeService {

    private final PullRequestService pullRequestService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final ApplicationEventPublisher eventPublisher;

    public Answer initializeAnswer(long questionId, User user) {
        PullRequest pullRequest = pullRequestService.findByQuestionId(questionId);
        if (pullRequest.isPending()) {
            pullRequestService.updateStatus(pullRequest.getId(), RecordStatus.PROGRESS);
        }
        return questionService.initializeAnswer(questionId, user);
    }

    @Transactional
    public Answer updateAnswer(long answerId, String updateContent, long userId) {
        Answer answer = questionService.updateAnswer(answerId, updateContent);
        eventPublisher.publishEvent(new UpdateAnswerEvent(this, answer, userId));
        return answer;
    }

    @Transactional
    public Answers updateAllAnswers(AnswerPutRequests updateRequests) {
        List<AnswerUpdateCommand> updateCommands = updateRequests.answers()
                .stream()
                .map(request -> new AnswerUpdateCommand(request.answerId(), request.content()))
                .toList();
        return questionService.updateAllAnswers(updateCommands);
    }

    public void deleteAnswer(long answerId) {
        Answer answer = answerService.findById(answerId);
        PullRequest pullRequest = pullRequestService.findByQuestionId(answer.getQuestionId());
        long answerCount = answerService.getAnswerCountByPullRequestId(pullRequest.getId());
        if (answerCount == 1) {
            pullRequestService.updateStatus(pullRequest.getId(), RecordStatus.PENDING);
        }
        questionService.deleteAnswer(answerId);
    }
}
