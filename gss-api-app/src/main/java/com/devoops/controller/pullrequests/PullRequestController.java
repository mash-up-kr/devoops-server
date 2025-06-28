package com.devoops.controller.pullrequests;

import com.devoops.controller.auth.AuthUser;
import com.devoops.domain.entity.user.User;
import com.devoops.dto.response.PullRequestReadResponse;
import com.devoops.service.pullrequests.PullRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PullRequestController {

    private final PullRequestService pullRequestService;

    @GetMapping("/api/pull-requests/{pullRequestId}")
    public ResponseEntity<PullRequestReadResponse> getPullRequest(
            @AuthUser User user,
            @PathVariable long pullRequestId
    ) {

        return ResponseEntity.ok().build();
    }


    @PatchMapping("/api/pull-requests/{pullRequestId}/done")
    public ResponseEntity<Void> pullRequestUpdateToDone(
            @AuthUser User user,
            @PathVariable(name = "pullRequestId") long pullRequestId
    ) {
        pullRequestService.updateToDone(pullRequestId);
        return ResponseEntity.ok().build();
    }
}
