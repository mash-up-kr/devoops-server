package com.devoops.controller.repository;

import com.devoops.controller.auth.AuthUser;
import com.devoops.domain.entity.github.PullRequests;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.RepositoryPullRequestResponses;
import com.devoops.service.repository.RepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RepositoryController {

    private final RepositoryService repositoryService;

    @GetMapping("/api/repositories/{repositoryId}/pull-requests")
    public ResponseEntity<RepositoryPullRequestResponses> getRepositoryPullRequests(
            @AuthUser User user,
            @PathVariable(name = "repositoryId") long repositoryId,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "page") int page
    ) {
        PullRequests pullRequests = repositoryService.getPullRequestsByRepository(user, repositoryId, size, page);
        RepositoryPullRequestResponses response = RepositoryPullRequestResponses.from(pullRequests);
        return ResponseEntity.ok(response);
    }
}

