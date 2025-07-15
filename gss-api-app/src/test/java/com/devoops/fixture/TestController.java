package com.devoops.fixture;

import com.devoops.controller.auth.AuthUser;
import com.devoops.domain.entity.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/method")
    public ResponseEntity<Void> testMethod(
            @AuthUser User user
    ) {
        return ResponseEntity.ok().build();
    }
}
