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
import com.devoops.service.repository.RepositoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repositories")
public class RepositoryController implements RepositoryControllerSwagger {

    private final RepositoryFacadeService repositoryFacadeService;
    private final RepositoryService repositoryService;

    @GetMapping("/{repositoryId}/pull-requests")
    public ResponseEntity<RepositoryPullRequestResponses> getRepositoryPullRequests(
            @AuthUser User user,
            @PathVariable(name = "repositoryId") long repositoryId,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "page") int page
    ) {
        PullRequests pullRequests = repositoryFacadeService.findAllPullRequestsByRepository(user, repositoryId, size, page);
        RepositoryPullRequestResponses response = RepositoryPullRequestResponses.from(pullRequests);
        return ResponseEntity.ok(response);
    }

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

    @GetMapping("/me")
    public ResponseEntity<MyRepositoriesResponse> getMyRepositories(@AuthUser User user) {
        List<GithubRepository> repositories = repositoryService.getMyRepositories(user);
        return ResponseEntity.ok(MyRepositoriesResponse.from(repositories));
    }
}

