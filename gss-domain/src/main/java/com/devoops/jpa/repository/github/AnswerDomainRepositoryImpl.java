package com.devoops.jpa.repository.github;

import com.devoops.domain.repository.github.AnswerDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnswerDomainRepositoryImpl implements AnswerDomainRepository {

    private final AnswerJpaRepository answerJpaRepository;
}
