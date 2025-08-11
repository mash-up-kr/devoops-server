package com.devoops.service.answer;

import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.repository.github.answer.AnswerDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerDomainRepository answerDomainRepository;

    public long getAnswerCountByPullRequestId(long pullRequestId) {
        return answerDomainRepository.getAnswerCountByPullRequestId(pullRequestId);
    }

    public Answer findById(long answerId) {
        return answerDomainRepository.findById(answerId);
    }
}
