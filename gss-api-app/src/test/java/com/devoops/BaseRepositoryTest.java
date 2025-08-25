package com.devoops;

import com.devoops.domain.repository.analysis.AiChargeRepository;
import com.devoops.generator.AiChargeGenerator;
import com.devoops.jpa.repository.analysis.AiChargeJpaRepository;
import com.devoops.jpa.repository.analysis.AiChargeRepositoryImpl;
import com.devoops.jpa.repository.github.repo.GithubRepoJpaRepository;
import com.devoops.jpa.repository.github.pr.PullRequestJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
        AiChargeRepository.class,
        AiChargeRepositoryImpl.class,
        AiChargeJpaRepository.class,
        AiChargeGenerator.class,
})
public abstract class BaseRepositoryTest {


    @Autowired
    protected AiChargeGenerator aiChargeGenerator;
}
