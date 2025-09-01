package com.devoops.generator;

import com.devoops.domain.entity.github.pr.ProcessingStatus;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.domain.repository.github.pr.PullRequestDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PullRequestGenerator {

    @Autowired
    private PullRequestDomainRepository pullRequestDomainRepository;

    public PullRequest generate(
            String title,
            RecordStatus status,
            ProcessingStatus processingStatus,
            GithubRepository githubRepository,
            LocalDateTime mergedAt
    ) {
        PullRequest pullRequest = new PullRequest(
            null,
            githubRepository.getId(),
            githubRepository.getUserId(),
            title,
            "description",
            "summary",
            "summaryDetail",
            "https://github.com/owner/repo/pull/2",
            1L,
            status,
            processingStatus,
            mergedAt,
            "tag"
        );
        return pullRequestDomainRepository.save(pullRequest);
    }
}
