package com.devoops.controller.user;

import com.devoops.dto.response.UserReadResponse;
import com.devoops.service.facade.UserFacadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacadeService userFacadeService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserReadResponse> getUserInfo(@PathVariable("userId") long userId) {
        UserReadResponse response = userFacadeService.getUser(userId);
        return ResponseEntity.ok(response);
    }
}
