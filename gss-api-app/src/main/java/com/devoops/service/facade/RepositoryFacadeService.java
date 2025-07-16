package com.devoops.service.facade;

import static com.devoops.Constants.INITIAL_PULL_REQUEST_COUNT;

import com.devoops.command.request.RepositoryCreateCommand;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.GithubRepoUrl;
import com.devoops.dto.request.RepositorySaveRequest;
import com.devoops.dto.response.GithubRepoInfoResponse;
import com.devoops.event.AnalyzeMyPrEvent;
import com.devoops.service.GitHubService;
import com.devoops.service.repository.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RepositoryFacadeService {

    private final RepositoryService repositoryService;
    private final GitHubService gitHubService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public GithubRepository save(RepositorySaveRequest request, User user) {
        GithubRepoUrl repoUrl = new GithubRepoUrl(request.url());
        GithubRepository savedRepository = saveRepository(repoUrl, user);
        gitHubService.registerWebhook(user, savedRepository.getId());
        eventPublisher.publishEvent(new AnalyzeMyPrEvent(repoUrl, user, this));
        return savedRepository;
    }

    private GithubRepository saveRepository(GithubRepoUrl url, User user) {
        GithubRepoInfoResponse repositoryInfo = gitHubService.getRepositoryInfo(url, user.getGithubToken());
        RepositoryCreateCommand createCommand = new RepositoryCreateCommand(
                user.getId(),
                repositoryInfo.name(),
                url.getUrl(),
                repositoryInfo.getOwnerName(),
                INITIAL_PULL_REQUEST_COUNT,
                repositoryInfo.id()
        );
        return repositoryService.save(createCommand);
    }

    public PullRequests findAllPullRequestsByRepository(User user, long repositoryId, int size, int page) {
        return repositoryService.getPullRequestsByRepository(user, repositoryId, size, page);
    }

    public PullRequests findAllPullRequests(User user, int size, int page) {
        return repositoryService.getPullRequests(user, size, page);
    }
}
