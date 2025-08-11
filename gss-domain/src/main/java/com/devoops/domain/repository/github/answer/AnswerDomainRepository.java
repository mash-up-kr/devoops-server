package com.devoops.domain.repository.github.answer;

import com.devoops.domain.entity.github.answer.Answer;
import com.devoops.domain.entity.github.question.Question;
import java.util.List;
import java.util.Optional;

public interface AnswerDomainRepository {

    Answer save(Answer answer);

    Answer findById(long answerId);

    Optional<Answer> findByQuestionId(long questionId);

    long getAnswerCountByPullRequestId(long pullRequestId);

    Answer updateById(long answerId, String content);

    void deleteById(long answerId);

    void deleteAllInQuestions(List<Question> questions);
}
