package com.devoops.service.repository;

import com.devoops.command.request.RepositoryCreateCommand;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final GithubRepoDomainRepository repoRepository;
    private final PullRequestDomainRepository pullRequestRepository;

    public GithubRepository save(RepositoryCreateCommand command) {
        return repoRepository.save(command.toDomainEntity());
    }

    public PullRequests getPullRequestsByRepository(User user, long repositoryId, int size, int page) {
        validateOwn(user, repositoryId);
        return pullRequestRepository.findPullRequestsByRepositoryIdOrderByMergedAt(repositoryId, size, page);
    }

    private void validateOwn(User user, long repositoryId) {
        if (!repoRepository.existsByIdAndUserId(repositoryId, user.getId())) {
            throw new GssException(ErrorCode.REPOSITORY_NOT_FOUND);
        }
    }

    public List<GithubRepository> getMyRepositories(User user) {
        return repoRepository.findByUserId(user.getId());
    }
}
