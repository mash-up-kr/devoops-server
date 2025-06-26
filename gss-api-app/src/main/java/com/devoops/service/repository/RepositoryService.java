package com.devoops.service.repository;

import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final GithubRepoDomainRepository repoRepository;
    private final PullRequestDomainRepository pullRequestRepository;

    public PullRequests getPullRequestsByRepository(User user, long repositoryId, int size, int page) {
        validateOwn(user, repositoryId);
        return pullRequestRepository.findPullRequestsByRepositoryIdOrderByMergedAt(repositoryId, size, page);
    }

    private void validateOwn(User user, long repositoryId) {
        if(!repoRepository.existsByIdAndUserId(repositoryId, user.getId())) {
            throw new GssException(ErrorCode.REPOSITIORY_NOT_FOUND);
        }
    }
}
