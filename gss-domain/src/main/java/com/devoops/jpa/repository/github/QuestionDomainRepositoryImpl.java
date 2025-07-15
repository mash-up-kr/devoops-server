package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.PullRequestEntity;
import com.devoops.jpa.entity.github.QuestionEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class QuestionDomainRepositoryImpl implements QuestionDomainRepository {

    private final QuestionJpaRepository questionRepository;
    private final PullRequestJpaRepository pullRequestRepository;

    @Transactional
    public Question save(Question question) {
        PullRequestEntity pullRequest = pullRequestRepository.findById(question.getPullRequestId())
                .orElseThrow(() -> new GssException(ErrorCode.PULL_REQUEST_NOT_FOUND));
        QuestionEntity questionEntity = QuestionEntity.from(question, pullRequest);
        QuestionEntity savedQuestion = questionRepository.save(questionEntity);
        return savedQuestion.toDomainEntity();
    }

    @Transactional(readOnly = true)
    public Question findById(long questionId) {
        QuestionEntity questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new GssException(ErrorCode.QUESTION_NOT_FOUND));
        return questionEntity.toDomainEntity();
    }

    @Transactional(readOnly = true)
    public List<QuestionAnswer> findAllPrQuestions(long pullRequestId) {
        return questionRepository.findByPullRequestId(pullRequestId);
    }

    @Override
    @Transactional
    public void saveAll(List<Question> question, long pullRequestId) {
        PullRequestEntity pullRequest = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new GssException(ErrorCode.PULL_REQUEST_NOT_FOUND));
        List<QuestionEntity> questionEntityList = question.stream()
                .map(q -> QuestionEntity.from(q, pullRequest))
                .toList();
        questionRepository.saveAll(questionEntityList);
    }
}
