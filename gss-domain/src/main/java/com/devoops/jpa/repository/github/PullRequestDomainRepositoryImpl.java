package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.PullRequest;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.github.RecordStatus;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.GithubRepositoryEntity;
import com.devoops.jpa.entity.github.PullRequestEntity;
import com.devoops.jpa.entity.github.QuestionEntity;
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
    private final QuestionJpaRepository questionJpaRepository;

    @Override
    @Transactional
    public PullRequest save(PullRequest pullRequest) {
        GithubRepositoryEntity githubRepositoryEntity = githubRepoRepository.findById(pullRequest.getRepositoryId())
                .orElseThrow(() -> new GssException(ErrorCode.GITHUB_REPOSITORY_NOT_FOUND));
        githubRepositoryEntity.plusPrCount();
        PullRequestEntity pullRequestEntity = PullRequestEntity.from(pullRequest);
        PullRequestEntity savedPullRequest = pullRequestRepository.save(pullRequestEntity);
        return savedPullRequest.toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public PullRequest findById(long pullRequestId) {
        PullRequestEntity pullRequestEntity = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new GssException(ErrorCode.PULL_REQUEST_NOT_FOUND));
        return pullRequestEntity.toDomainEntity();
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
    @Transactional(readOnly = true)
    public PullRequests findUserPullRequestsOrderByMergedAt(long userId, int size, int page) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "mergedAt"));
        return pullRequestRepository.findByUserId(userId, pageable)
                .get()
                .map(PullRequestEntity::toDomainEntity)
                .collect(Collectors.collectingAndThen(Collectors.toList(), PullRequests::new));
    }

    @Override
    @Transactional
    public PullRequest updateStatus(long pullRequestId, RecordStatus status) {
        PullRequestEntity pullRequest = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new GssException(ErrorCode.PULL_REQUEST_NOT_FOUND));
        pullRequest.updateStatus(status);
        return pullRequest.toDomainEntity();
    }

    @Override
    @Transactional
    public PullRequest updateAnalyzedResult(long pullRequestId, String summary, String detailSummary) {
        PullRequestEntity pullRequest = pullRequestRepository.findById(pullRequestId)
                .orElseThrow(() -> new GssException(ErrorCode.PULL_REQUEST_NOT_FOUND));
        pullRequest.updateAnalyzeResult(summary, detailSummary);
        return pullRequest.toDomainEntity();
    }

    @Override
    @Transactional(readOnly = true)
    public PullRequest findByQuestionId(long questionId) {
        QuestionEntity questionEntity = questionJpaRepository.findById(questionId)
                .orElseThrow(() -> new GssException(ErrorCode.QUESTION_NOT_FOUND));
        return pullRequestRepository.findById(questionEntity.getPullRequestId())
                .orElseThrow(() -> new GssException(ErrorCode.PULL_REQUEST_NOT_FOUND))
                .toDomainEntity();
    }
}
