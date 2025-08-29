package com.devoops.service.repository;

import com.devoops.command.request.RepositoryCreateCommand;
import com.devoops.domain.entity.github.pr.PullRequests;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.pr.PullRequestDomainRepository;
import com.devoops.domain.repository.github.repo.GithubRepoDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final GithubRepoDomainRepository repoRepository;
    private final PullRequestDomainRepository pullRequestRepository;

    public GithubRepository save(RepositoryCreateCommand command) {
        return repoRepository.save(command.toDomainEntity());
    }

    public Optional<GithubRepository> findByUserAndExternalId(User user, long repositoryId) {
        return repoRepository.findByExternalIdAndUserId(repositoryId, user.getId());
    }

    public GithubRepository reTracking(User user, long repositoryId) {
        GithubRepository repo = repoRepository.getByExternalIdAndUserId(repositoryId, user.getId());
        GithubRepository reTrackingRepo = repo.reTracking();
        return repoRepository.update(reTrackingRepo);
    }

    public PullRequests getProcessedPullRequests(User user, int size, int page) {
        return pullRequestRepository.findProcessedUserPullRequestsOrderByMergedAt(user.getId(), size, page);
    }

    public PullRequests getProcessedPullRequestsByRepository(User user, long repositoryId, int size, int page) {
        validateOwn(user, repositoryId);
        return pullRequestRepository.findProcessedPullRequestsByRepositoryIdOrderByMergedAt(repositoryId, size, page);
    }

    private void validateOwn(User user, long repositoryId) {
        if (!repoRepository.existsByIdAndUserId(repositoryId, user.getId())) {
            throw new GssException(ErrorCode.REPOSITORY_NOT_FOUND);
        }
    }

    public List<GithubRepository> getMyRepositories(User user) {
        return repoRepository.findByUserId(user.getId());
    }

    @Transactional
    public void stopTrackingRepository(User user, long repositoryId) {
        GithubRepository repo = repoRepository.findByIdAndUserId(repositoryId, user.getId());
        GithubRepository updatedRepository = repo.stopTracking();
        repoRepository.update(updatedRepository);
    }
}
