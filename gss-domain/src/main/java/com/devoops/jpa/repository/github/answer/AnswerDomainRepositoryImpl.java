package com.devoops.jpa.repository.github.answer;

import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.question.Question;
import com.devoops.domain.repository.github.answer.AnswerDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.answer.AnswerEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class AnswerDomainRepositoryImpl implements AnswerDomainRepository {

    private final AnswerJpaRepository answerJpaRepository;

    @Override
    @Transactional
    public Answer save(Answer answer) {
        AnswerEntity answerEntity = AnswerEntity.from(answer);
        AnswerEntity savedAnswer = answerJpaRepository.save(answerEntity);
        return savedAnswer.toDomainEntity();
    }

    @Override
    public Answer findById(long answerId) {
        return answerJpaRepository.findById(answerId)
                .orElseThrow(() -> new GssException(ErrorCode.ANSWER_NOT_FOUND))
                .toDomainEntity();
    }

    @Override
    public Optional<Answer> findByQuestionId(long questionId) {
        return answerJpaRepository.findByQuestionId(questionId)
                .map(AnswerEntity::toDomainEntity);
    }

    @Override
    public long getAnswerCountByPullRequestId(long pullRequestId) {
        return answerJpaRepository.getAnswerCountByPullRequestId(pullRequestId);
    }

    @Override
    @Transactional
    public Answer updateById(long answerId, String content) {
        AnswerEntity answerEntity = answerJpaRepository.findById(answerId)
                .orElseThrow(() -> new GssException(ErrorCode.ANSWER_NOT_FOUND));
        answerEntity.update(content);
        return answerEntity.toDomainEntity();
    }

    @Override
    @Transactional
    public void deleteById(long answerId) {
        answerJpaRepository.deleteById(answerId);
    }

    @Override
    public void deleteAllInQuestions(List<Question> questions) {
        List<Long> questionIds = questions.stream()
                .map(Question::getId)
                .toList();
        answerJpaRepository.deleteByQuestionIdIn(questionIds);
    }
}
