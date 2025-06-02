package com.devoops.controller;

import com.devoops.dto.request.UserSaveRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @PostMapping("/api/auth/github")
    public ResponseEntity<?> issueToken(@RequestBody @Valid UserSaveRequest userSaveRequest) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/auth/github/reissue")
    public ResponseEntity<?> reIssueToken() {
        return ResponseEntity.ok().build();
    }
}
