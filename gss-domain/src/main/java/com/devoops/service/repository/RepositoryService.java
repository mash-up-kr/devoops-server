package com.devoops.service.repository;

import com.devoops.command.request.RepositoryCreateCommand;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.github.Question;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.AnswerDomainRepository;
import com.devoops.domain.repository.github.AnswerRankingDomainRepository;
import com.devoops.domain.repository.github.GithubRepoDomainRepository;
import com.devoops.domain.repository.github.PullRequestDomainRepository;
import com.devoops.domain.repository.github.QuestionDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepositoryService {

    private final GithubRepoDomainRepository repoRepository;
    private final PullRequestDomainRepository pullRequestRepository;
    private final AnswerRankingDomainRepository answerRankingRepository;
    private final AnswerDomainRepository answerRepository;
    private final QuestionDomainRepository questionRepository;

    public GithubRepository save(RepositoryCreateCommand command) {
        if(repoRepository.existsByExternalId(command.externalId())) {
            throw new GssException(ErrorCode.ALREADY_SAVED_REPOSITORY);
        }
        return repoRepository.save(command.toDomainEntity());
    }

    public PullRequests getPullRequests(User user, int size, int page) {
        return pullRequestRepository.findUserPullRequestsOrderByMergedAt(user.getId(), size, page);
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

    @Transactional
    public void delete(User user, long repositoryId) {
        GithubRepository repo = repoRepository.findByIdAndUserId(repositoryId, user.getId());
        repoRepository.deleteById(repo.getId());
        PullRequests repositoryPrs = pullRequestRepository.findByRepositoryId(repo.getId());
        pullRequestRepository.deleteAll(repositoryPrs);
        answerRankingRepository.deleteAllInPullRequests(repositoryPrs);
        List<Question> questions = questionRepository.findAllByPullRequests(repositoryPrs);
        questionRepository.deleteAll(questions);
        answerRepository.deleteAllInQuestions(questions);
    }
}
