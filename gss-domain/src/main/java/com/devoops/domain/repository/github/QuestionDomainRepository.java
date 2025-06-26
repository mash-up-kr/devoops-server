package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.Question;

public interface QuestionDomainRepository {

    Question findByIdAndUserId(long questionId, long userId);

    Question save(Question question);
}
