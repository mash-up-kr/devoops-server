package com.devoops.generator;

import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PullRequestGenerator {

    @Autowired
    private PullRequestDomainRepository pullRequestDomainRepository;

    public PullRequest generate(String title, RecordStatus status, GithubRepository githubRepository, LocalDateTime mergedAt) {
        PullRequest pullRequest = new PullRequest(
                null,
                githubRepository.getId(),
                githubRepository.getUserId(),
                title,
                "description",
                "summary",
                1L,
                status,
                mergedAt,
                "tag"
                );
        return pullRequestDomainRepository.save(pullRequest);
    }
}
