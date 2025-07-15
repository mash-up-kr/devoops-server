package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.QuestionAnswer;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.QuestionEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class QuestionDomainRepositoryImpl implements QuestionDomainRepository {

    private final QuestionJpaRepository questionRepository;

    @Override
    @Transactional
    public Question save(Question question) {
        QuestionEntity questionEntity = QuestionEntity.from(question);
        QuestionEntity savedQuestion = questionRepository.save(questionEntity);
        return savedQuestion.toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public Question findById(long questionId) {
        QuestionEntity questionEntity = questionRepository.findById(questionId)
                .orElseThrow(() -> new GssException(ErrorCode.QUESTION_NOT_FOUND));
        return questionEntity.toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionAnswer> findAllPrQuestions(long pullRequestId) {
        return questionRepository.findByPullRequestId(pullRequestId);
    }

    @Override
    @Transactional
    public void saveAll(List<Question> question, long pullRequestId) {
        List<QuestionEntity> questionEntityList = question.stream()
                .map(QuestionEntity::from)
                .toList();
        questionRepository.saveAll(questionEntityList);
    }
}
