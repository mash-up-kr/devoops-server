package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.jpa.entity.github.GithubRepositoryEntity;
import com.devoops.jpa.entity.github.PullRequestEntity;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PullRequestDomainRepositoryImpl implements PullRequestDomainRepository {

    private final PullRequestJpaRepository pullRequestRepository;
    private final GithubRepoJpaRepository githubRepoRepository;

    @Override
    public PullRequest save(PullRequest pullRequest) {
        GithubRepositoryEntity githubRepositoryEntity = githubRepoRepository.findById(pullRequest.getRepositoryId()).get();
        PullRequestEntity pullRequestEntity = PullRequestEntity.from(pullRequest, githubRepositoryEntity);
        PullRequestEntity savedPullRequest = pullRequestRepository.save(pullRequestEntity);
        return savedPullRequest.toDomainEntity();
    }

    @Override
    public PullRequests findPullRequestsByRepositoryIdOrderByCreatedAt(long repositoryId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return pullRequestRepository.findByRepositoryId(repositoryId, pageable).get()
                .map(PullRequestEntity::toDomainEntity)
                .collect(Collectors.collectingAndThen(Collectors.toList(), PullRequests::new));
    }
}
