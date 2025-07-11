package com.devoops.domain.repository.github;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.github.RecordStatus;

public interface PullRequestDomainRepository {

    PullRequest save(PullRequest pullRequest);

    PullRequest findById(long pullRequestId);

    PullRequests findPullRequestsByRepositoryIdOrderByMergedAt(long repositoryId, int size, int page);

    PullRequests findUserPullRequestsOrderByMergedAt(long userId, int size, int page);

    PullRequest findByQuestionId(long questionId);

    PullRequest updateStatus(long pullRequestId, RecordStatus status);
}
