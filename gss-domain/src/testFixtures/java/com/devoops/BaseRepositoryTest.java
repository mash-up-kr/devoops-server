package com.devoops;

import com.devoops.jpa.repository.github.GithubRepoJpaRepository;
import com.devoops.jpa.repository.github.PullRequestJpaRepository;
import com.devoops.jpa.repository.user.UserJpaRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({PullRequestJpaRepository.class, GithubRepoJpaRepository.class, UserJpaRepository.class})
public abstract class BaseRepositoryTest {

}
