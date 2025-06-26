package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.exception.GssRepositoryException;
import com.devoops.exception.RepositoryErrorCode;
import com.devoops.jpa.entity.github.GithubRepositoryEntity;
import com.devoops.jpa.entity.github.PullRequestEntity;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class PullRequestDomainRepositoryImpl implements PullRequestDomainRepository {

    private final PullRequestJpaRepository pullRequestRepository;
    private final GithubRepoJpaRepository githubRepoRepository;

    @Override
    @Transactional
    public PullRequest save(PullRequest pullRequest) {
        GithubRepositoryEntity githubRepositoryEntity = githubRepoRepository.findById(pullRequest.getRepositoryId())
                .orElseThrow(() -> new GssRepositoryException(RepositoryErrorCode.GITHUB_REPOSITORY_NOT_FOUND));
        PullRequestEntity pullRequestEntity = PullRequestEntity.from(pullRequest, githubRepositoryEntity);
        PullRequestEntity savedPullRequest = pullRequestRepository.save(pullRequestEntity);
        return savedPullRequest.toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public PullRequests findPullRequestsByRepositoryIdOrderByMergedAt(long repositoryId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "mergedAt"));
        return pullRequestRepository.findByRepositoryId(repositoryId, pageable)
                .get()
                .map(PullRequestEntity::toDomainEntity)
                .collect(Collectors.collectingAndThen(Collectors.toList(), PullRequests::new));
    }

    @Override
    @Transactional
    public PullRequest updateToDone(long pullRequestId) {
        PullRequestEntity pullRequest = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new GssRepositoryException(RepositoryErrorCode.PULL_REQUEST_NOT_FOUND));
        pullRequest.updateToDone();
        return pullRequest.toDomainEntity();
    }
}
