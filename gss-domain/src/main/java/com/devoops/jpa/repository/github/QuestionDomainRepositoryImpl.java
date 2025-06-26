package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.Question;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.exception.GssRepositoryException;
import com.devoops.exception.RepositoryErrorCode;
import com.devoops.jpa.entity.github.PullRequestEntity;
import com.devoops.jpa.entity.github.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionDomainRepositoryImpl implements QuestionDomainRepository {

    private final QuestionJpaRepository questionRepository;
    private final PullRequestJpaRepository pullRequestRepository;

    public Question findById(long questionId) {
        QuestionEntity questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new GssRepositoryException(RepositoryErrorCode.QUESTION_NOT_FOUND));
        return questionEntity.toDomainEntity();
    }

    public Question save(Question question) {
        PullRequestEntity pullRequest = pullRequestRepository.findById(question.getPullRequestId())
                .orElseThrow(() -> new GssRepositoryException(RepositoryErrorCode.PULL_REQUEST_NOT_FOUND));
        QuestionEntity questionEntity = QuestionEntity.from(question, pullRequest);
        QuestionEntity savedQuestion = questionRepository.save(questionEntity);
        return savedQuestion.toDomainEntity();
    }
}
