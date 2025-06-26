package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.PullRequests;

public interface PullRequestDomainRepository {

    PullRequests findPullRequestsByRepositoryIdOrderByCreatedAt(long repositoryId, int size, int page);

}
