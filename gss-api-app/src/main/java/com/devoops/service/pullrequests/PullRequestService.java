package com.devoops.service.pullrequests;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PullRequestService {

    private final PullRequestDomainRepository pullRequestRepository;

    public PullRequest updateToDone(long pullRequestId) {
        return pullRequestRepository.updateToDone(pullRequestId);
    }
}
