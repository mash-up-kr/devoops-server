package com.devoops.service.facade;

import static com.devoops.Constants.INITIAL_PULL_REQUEST_COUNT;

import com.devoops.command.request.RepositoryCreateCommand;
import com.devoops.domain.entity.github.pr.PullRequests;
import com.devoops.domain.entity.github.repo.GithubRepository;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.GithubRepoUrl;
import com.devoops.dto.request.RepositorySaveRequest;
import com.devoops.dto.response.GithubRepoInfoResponse;
import com.devoops.event.AnalyzeMyPrEvent;
import com.devoops.exception.GithubNotFoundException;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.service.GitHubService;
import com.devoops.service.github.WebHookService;
import com.devoops.service.repository.RepositoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepositoryFacadeService {

    private final RepositoryService repositoryService;
    private final GitHubService gitHubService;
    private final WebHookService webHookService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public GithubRepository save(RepositorySaveRequest request, User user) {
        try {
            GithubRepoUrl repoUrl = new GithubRepoUrl(request.url());
            GithubRepoInfoResponse repositoryInfo = gitHubService.getRepositoryInfo(repoUrl, user.getGithubToken());
            GithubRepository savedRepository = repositoryService.findByUserAndExternalId(user, repositoryInfo.id())
                            .map(alreadyRegisteredRepo -> reTrackingOrThrowException(user, alreadyRegisteredRepo))
                            .orElseGet(() -> registerNewRepo(repositoryInfo, repoUrl, user));

            eventPublisher.publishEvent(new AnalyzeMyPrEvent(repoUrl, user, this));
            return savedRepository;
        } catch (GithubNotFoundException githubNotFoundException) {
            throw new GssException(ErrorCode.REGISTRY_GITHUB_REPOSITORY_NOT_FOUND);
        }
    }

    private GithubRepository registerNewRepo(
            GithubRepoInfoResponse repositoryInfo,
            GithubRepoUrl url,
            User user
    ) {
        RepositoryCreateCommand createCommand = new RepositoryCreateCommand(
                user.getId(),
                repositoryInfo.name(),
                url.getUrl(),
                repositoryInfo.getOwnerName(),
                INITIAL_PULL_REQUEST_COUNT,
                repositoryInfo.id()
        );
        GithubRepository savedRepository = repositoryService.save(createCommand);
        webHookService.registerWebhook(user, savedRepository.getId());
        return savedRepository;
    }

    private GithubRepository reTrackingOrThrowException(User user, GithubRepository registeredRepo) {
        if(registeredRepo.isTracking()) {
            throw new GssException(ErrorCode.ALREADY_SAVED_REPOSITORY);
        }
        return repositoryService.reTracking(user, registeredRepo.getExternalId());
    }

    public PullRequests findAllPullRequestsByRepository(User user, long repositoryId, int size, int page) {
        return repositoryService.getPullRequestsByRepository(user, repositoryId, size, page);
    }

    public PullRequests findAllPullRequests(User user, int size, int page) {
        return repositoryService.getPullRequests(user, size, page);
    }

    public List<GithubRepository> findMyRepositories(User user) {
        return repositoryService.getMyRepositories(user);
    }

    @Transactional
    public void deleteRepository(User user, long repositoryId) {
        //TODO 다른 유저로부터 등록된 상황 고려
        repositoryService.stopTrackingRepository(user, repositoryId);
        webHookService.deleteWebhook(user, repositoryId);
    }
}
