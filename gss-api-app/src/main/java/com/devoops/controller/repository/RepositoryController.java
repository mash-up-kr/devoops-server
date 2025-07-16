package com.devoops.controller.repository;

import com.devoops.controller.auth.AuthUser;
import com.devoops.controller.docs.RepositoryControllerSwagger;
import com.devoops.domain.entity.github.GithubRepository;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.request.RepositorySaveRequest;
import com.devoops.dto.response.MyRepositoriesResponse;
import com.devoops.dto.response.RepositoryPullRequestResponses;
import com.devoops.dto.response.RepositorySaveResponse;
import com.devoops.service.facade.RepositoryFacadeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repositories")
public class RepositoryController implements RepositoryControllerSwagger {

    private final RepositoryFacadeService repositoryFacadeService;

    @Override
    @GetMapping("/{repositoryId}/pull-requests")
    public ResponseEntity<RepositoryPullRequestResponses> getRepositoryPullRequests(
            @AuthUser User user,
            @PathVariable(name = "repositoryId") long repositoryId,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "page") int page
    ) {
        PullRequests pullRequests = repositoryFacadeService.findAllPullRequestsByRepository(user, repositoryId, size,
                page);
        RepositoryPullRequestResponses response = RepositoryPullRequestResponses.from(pullRequests);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/pull-requests")
    public ResponseEntity<RepositoryPullRequestResponses> getRepositoryEntirePullRequests(
            @AuthUser User user,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "page") int page
    ) {
        PullRequests pullRequests = repositoryFacadeService.findAllPullRequests(user, size, page);
        RepositoryPullRequestResponses response = RepositoryPullRequestResponses.from(pullRequests);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping
    public ResponseEntity<RepositorySaveResponse> saveRepository(
            @AuthUser User user,
            @Valid @RequestBody RepositorySaveRequest request
    ) {
        GithubRepository savedRepository = repositoryFacadeService.save(request, user);
        RepositorySaveResponse response = new RepositorySaveResponse(savedRepository);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<MyRepositoriesResponse> getMyRepositories(@AuthUser User user) {
        List<GithubRepository> repositories = repositoryFacadeService.findMyRepositories(user);
        return ResponseEntity.ok(MyRepositoriesResponse.from(repositories));
    }

    @Override
    @DeleteMapping("/{repositoryId}")
    public ResponseEntity<Void> deleteRepositories(
            @AuthUser User user,
            @PathVariable(name = "repositoryId") long repositoryId
    ) {
        repositoryFacadeService.deleteRepository(user, repositoryId);
        return ResponseEntity.noContent().build();
    }
}

