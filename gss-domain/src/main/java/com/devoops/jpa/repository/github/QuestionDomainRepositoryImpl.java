package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.Question;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.jpa.entity.github.QuestionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionDomainRepositoryImpl implements QuestionDomainRepository {

    private final QuestionJpaRepository questionRepository;

    public Question findByIdAndUserId(long questionId, long userId) {
        QuestionEntity questionEntity = questionRepository.findByIdAndUser_Id(questionId, userId);
        return questionEntity.toDomainEntity();
    }
}
