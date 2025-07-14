package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.github.QuestionAnswer;
import java.util.List;

public interface QuestionDomainRepository {

    Question findById(long questionId);

    Question save(Question question);

    List<QuestionAnswer> findAllPrQuestions(long pullRequestId);

    void saveAll(List<Question> questions);
}
