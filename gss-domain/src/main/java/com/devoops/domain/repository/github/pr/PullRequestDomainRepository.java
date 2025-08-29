package com.devoops.domain.repository.github.pr;

import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.pr.PullRequests;
import com.devoops.domain.entity.github.pr.RecordStatus;

public interface PullRequestDomainRepository {

    PullRequest save(PullRequest pullRequest);

    PullRequest findById(long pullRequestId);

    PullRequests findProcessedPullRequestsByRepositoryIdOrderByMergedAt(long repositoryId, int size, int page);

    PullRequest updateAnalyzedResult(long pullRequestId, String summary, String detailSummary);

    PullRequests findProcessedUserPullRequestsOrderByMergedAt(long userId, int size, int page);

    PullRequest findByQuestionId(long questionId);

    PullRequest updateStatus(long pullRequestId, RecordStatus status);
}
