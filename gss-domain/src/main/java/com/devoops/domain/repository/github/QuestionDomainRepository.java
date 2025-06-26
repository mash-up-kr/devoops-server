package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.Question;

public interface QuestionDomainRepository {

    Question findById(long questionId);

    Question save(Question question);
}
