package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.PullRequests;

public interface PullRequestDomainRepository {

    PullRequest save(PullRequest pullRequest);

    PullRequests findPullRequestsByRepositoryIdOrderByCreatedAt(long repositoryId, int size, int page);

}
