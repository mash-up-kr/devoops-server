package com.devoops.controller.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {

    @GetMapping("/api/repositories/{repositoryId}/pull-requests")
    public ResponseEntity<Void> getRepositoryPullRequests(
            @PathVariable(name = "repositoryId") long repositoryId,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "page") int page
    ) {
        return ResponseEntity.ok().build();
    }
}

