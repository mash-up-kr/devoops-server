package com.devoops.controller.pullrequests;

import com.devoops.controller.auth.AuthUser;
import com.devoops.controller.docs.PullRequestControllerSwagger;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.PullRequestDetailReadResponse;
import com.devoops.dto.response.PullRequestReadResponse;
import com.devoops.service.facade.PullRequestFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PullRequestController implements PullRequestControllerSwagger {

    private final PullRequestFacadeService pullRequestFacadeService;

    @GetMapping("/api/pull-requests/{pullRequestId}")
    public ResponseEntity<PullRequestDetailReadResponse> getDetailPullRequest(
            @AuthUser User user,
            @PathVariable long pullRequestId
    ) {
        PullRequestDetailReadResponse response = pullRequestFacadeService.detailRead(pullRequestId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/repositories/pull-requests/{pullRequestId}")
    public ResponseEntity<PullRequestReadResponse> getPullRequest(
            @AuthUser User user,
            @PathVariable long pullRequestId
    ) {
        PullRequestReadResponse response = pullRequestFacadeService.read(pullRequestId);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/api/pull-requests/{pullRequestId}/done")
    public ResponseEntity<Void> pullRequestUpdateToDone(
            @AuthUser User user,
            @PathVariable(name = "pullRequestId") long pullRequestId
    ) {
        pullRequestFacadeService.updateToDone(pullRequestId);
        return ResponseEntity.ok().build();
    }
}
