package com.devoops.jpa.repository.github;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnswerDomainRepositoryImpl {

    private final AnswerJpaRepository answerJpaRepository;
}
