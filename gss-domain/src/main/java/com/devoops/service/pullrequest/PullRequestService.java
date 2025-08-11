package com.devoops.service.pullrequest;

import com.devoops.command.request.PullRequestCreateCommand;
import com.devoops.domain.entity.github.pr.PullRequest;
import com.devoops.domain.entity.github.pr.PullRequests;
import com.devoops.domain.entity.github.pr.RecordStatus;
import com.devoops.domain.repository.github.pr.PullRequestDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PullRequestService {

    private final PullRequestDomainRepository pullRequestRepository;

    public PullRequest save(PullRequestCreateCommand command) {
        PullRequest pullRequest = command.toDomainEntity();
        return pullRequestRepository.save(pullRequest);
    }

    public PullRequest updateStatus(long pullRequestId, RecordStatus status) {
        return pullRequestRepository.updateStatus(pullRequestId, status);
    }

    public PullRequest updateAnalyzeResult(long pullRequestId, String summary, String detailSummary) {
        return pullRequestRepository.updateAnalyzedResult(pullRequestId, summary, detailSummary);
    }

    public PullRequest findByQuestionId(long questionId) {
        return pullRequestRepository.findByQuestionId(questionId);
    }

    public PullRequest getPullRequest(long pullRequestId) {
        return pullRequestRepository.findById(pullRequestId);
    }
}

