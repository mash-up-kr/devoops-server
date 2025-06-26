package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.Answer;
import com.devoops.domain.repository.github.AnswerDomainRepository;
import com.devoops.exception.GssRepositoryException;
import com.devoops.exception.RepositoryErrorCode;
import com.devoops.jpa.entity.github.AnswerEntity;
import com.devoops.jpa.entity.github.QuestionEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class AnswerDomainRepositoryImpl implements AnswerDomainRepository {

    private final AnswerJpaRepository answerJpaRepository;
    private final QuestionJpaRepository questionJpaRepository;

    @Override
    @Transactional
    public Answer save(Answer answer) {
        QuestionEntity question = questionJpaRepository.findById(answer.getQuestionId())
                .orElseThrow(() -> new GssRepositoryException(RepositoryErrorCode.QUESTION_NOT_FOUND));
        AnswerEntity answerEntity = AnswerEntity.from(answer, question);
        AnswerEntity savedAnswer = answerJpaRepository.save(answerEntity);
        return savedAnswer.toDomainEntity();
    }

    @Override
    @Transactional
    public Answer updateById(long answerId, String content) {
        AnswerEntity answerEntity = answerJpaRepository.findById(answerId)
                .orElseThrow(() -> new GssRepositoryException(RepositoryErrorCode.ANSWER_NOT_FOUND));
        answerEntity.update(content);
        return answerEntity.toDomainEntity();
    }

    @Override
    @Transactional
    public void deleteById(long answerId){
        answerJpaRepository.deleteById(answerId);
    }
}
