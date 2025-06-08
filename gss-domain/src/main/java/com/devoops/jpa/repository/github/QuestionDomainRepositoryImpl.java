package com.devoops.jpa.repository.github;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuestionDomainRepositoryImpl {

    private final QuestionJpaRepository questionRepository;
}
