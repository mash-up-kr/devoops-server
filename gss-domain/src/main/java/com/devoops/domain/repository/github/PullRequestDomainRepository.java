package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.PullRequests;

public interface PullRequestDomainRepository {

    PullRequest save(PullRequest pullRequest);

    PullRequest findById(long pullRequestId);

    PullRequests findPullRequestsByRepositoryIdOrderByMergedAt(long repositoryId, int size, int page);

    PullRequest updateToDone(long pullRequestId);

    PullRequest updateAnalyzedResult(long pullRequestId, String summary);
}
