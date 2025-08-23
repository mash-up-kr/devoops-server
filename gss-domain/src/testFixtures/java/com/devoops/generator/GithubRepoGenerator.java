package com.devoops.generator;

import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.repo.GithubRepoDomainRepository;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GithubRepoGenerator {

    @Autowired
    private GithubRepoDomainRepository githubRepoDomainRepository;

    public GithubRepository generate(User user, String repoName) {
        return generate(user, repoName, ThreadLocalRandom.current().nextLong(), true);
    }

    public GithubRepository generate(User user, String repoName, long externalId, boolean tracking) {
        GithubRepository repository = new GithubRepository(
                null,
                user.getId(),
                repoName,
                "url",
                "owner",
                0,
                externalId,
                tracking
        );
        return githubRepoDomainRepository.save(repository);
    }
}
