package com.devoops.generator;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GithubRepoGenerator {

    @Autowired
    private GithubRepoDomainRepository githubRepoDomainRepository;

    public GithubRepository generate(User user, String repoName) {
        GithubRepository repository = new GithubRepository(null, user.getId(), repoName, "url", 1L);
        return githubRepoDomainRepository.save(repository);
    }
}
